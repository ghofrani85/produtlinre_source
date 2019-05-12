package com.swp1718.productLinRe2.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.swp1718.productLinRe2.Application;
import com.swp1718.productLinRe2.database.helper.TrackingType;
import com.swp1718.productLinRe2.database.rowmapper.FeatureRowMapper;
import com.swp1718.productLinRe2.database.rowmapper.ProductRowMapper;
import com.swp1718.productLinRe2.database.rowmapper.ProjectRowMapper;
import com.swp1718.productLinRe2.model.Product;
import com.swp1718.productLinRe2.model.Project;

/**
 * Class to handle project interaction with the database.
 * 
 * @author Tobias Powelske
 *
 */
@Component
public class ProjectAccess {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	private static final String INSERT_SQL = "INSERT INTO projects(title, description, userid, "
			+ "parentid, updatedparent, last_change) VALUES(?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_SQL = "UPDATE projects SET title = ?, description = ?, "
			+ "parentid = ?, updatedparent = ?, last_change = ? WHERE id = ?";
	private static final String UPDATE_PARENT_SQL = "UPDATE projects SET parentid = ?, updatedparent = ? WHERE parentid = ?";
	private static final String DELETE_SQL = "DELETE FROM projects WHERE id = ?";
	private static final String SELECT_SQL = "SELECT id, title, description, userid, updatedparent, parentid, last_change FROM projects";
	private static final String INSERT_PROJECTSXFEATURES_SQL = "INSERT INTO projectsxfeatures(projectid, featureid) VALUES(?, ?)";
	private static final String DELETE_PROJECTSXFEATURES_SQL = "DELETE FROM projectsxfeatures WHERE projectid = ?";
	private static final String INSERT_PROJECTSXPRODUCTS_SQL = "INSERT INTO projectsxproducts(projectid, productid) VALUES(?, ?)";
	private static final String DELETE_PROJECTSXPRODUCTS_SQL = "DELETE FROM projectsxproducts WHERE projectid = ?";

	@Autowired
	private JdbcTemplate jdbc;

	@Autowired
	private FeatureAccess featacc;

	@Autowired
	private ProductAccess prodacc;

	@Autowired
	private TrackingAccess trackacc;

	@Autowired
	private UserAccess useracc;

	/**
	 * Prevent instances of the class.
	 */
	private ProjectAccess() {
	}

	/**
	 * Returns a list of all projects.
	 * 
	 * @return list of all projects
	 */
	public List<Project> selectProjects() {
		LinkedList<Project> result = new LinkedList<>();

		log.debug("Getting projects...");

		jdbc.query(SELECT_SQL, new ProjectRowMapper()).forEach(project -> result.add(project));

		result.forEach(proj -> {
			// get features for the project...
			selectProjectsXFeatures(proj);

			// get products for the project...
			selectProjectsXProducts(proj);

			// load tracking...
			proj.setTrackingList(trackacc.selectTrackingByItemID(proj.getId(), TrackingType.PROJECT));

			// load user...
			proj.setUser(useracc.selectUsersByID(proj.getUser().getId()));

			// load parent...
			if (proj.getParent() != null) {
				proj.setParent(selectProjectsByID(proj.getParent().getId()));
			}
		});

		return result;
	}

	/**
	 * Returns the project with the given id.
	 * 
	 * @param id
	 *            id of the project to be retrieved
	 * @return the retrieved project
	 */
	public Project selectProjectsByID(int id) {
		Project result = null;

		log.debug("Getting project with ID {}", id);

		List<Project> resultlist = jdbc.query(SELECT_SQL + " WHERE id = ?", new Object[] { id },
				new ProjectRowMapper());

		if (!resultlist.isEmpty()) {
			result = resultlist.get(0);

			// get features for the project...
			selectProjectsXFeatures(result);

			// get products for the project...
			selectProjectsXProducts(result);
			// load products using the ProductAccess-class to load sublists...
			LinkedList<Product> templist = new LinkedList<>();
			result.getProductList().forEach(prod -> {
				Product pr = prodacc.selectProductsByID(prod.getId());
				templist.add(pr);
			});
			result.setProductList(templist);

			// load tracking...
			result.setTrackingList(trackacc.selectTrackingByItemID(result.getId(), TrackingType.PROJECT));

			// load user...
			result.setUser(useracc.selectUsersByID(result.getUser().getId()));

			// load parent...
			if (result.getParent() != null) {
				result.setParent(selectProjectsByID(result.getParent().getId()));
			}
		}

		return result;
	}

	/**
	 * Returns the projects associated with the given feature id.
	 * 
	 * @param id
	 *            the id to select the projects
	 * @return a list of found projects
	 */
	public List<Project> selectProjectsByFeatureID(int id) {
		List<Project> result = null;

		result = jdbc.query(SELECT_SQL + " JOIN projectsxfeatures ON projectid = id WHERE featureid = ?",
				new Object[] { id }, new ProjectRowMapper());

		result.forEach(proj -> {
			// get features for the project...
			selectProjectsXFeatures(proj);

			// get products for the project...
			selectProjectsXProducts(proj);

			// load tracking...
			proj.setTrackingList(trackacc.selectTrackingByItemID(proj.getId(), TrackingType.PROJECT));

			// load user...
			proj.setUser(useracc.selectUsersByID(proj.getUser().getId()));

			// load parent...
			if (proj.getParent() != null) {
				proj.setParent(selectProjectsByID(proj.getParent().getId()));
			}
		});

		return result;
	}

	/**
	 * Returns the projects with the given id of the user who created them.
	 * 
	 * @param id
	 *            id of the user who created the projects
	 * @return a list of found projects
	 */
	public List<Project> selectProjectsByUserID(int id) {
		LinkedList<Project> result = new LinkedList<>();

		log.debug("Getting projects for user {}...", id);

		jdbc.query(SELECT_SQL + " WHERE userid = ?", new Object[] { id }, new ProjectRowMapper())
				.forEach(project -> result.add(project));

		result.forEach(proj -> {
			// get features for the project...
			selectProjectsXFeatures(proj);

			// get products for the project...
			selectProjectsXProducts(proj);

			// load tracking...
			proj.setTrackingList(trackacc.selectTrackingByItemID(proj.getId(), TrackingType.PROJECT));

			// load user...
			proj.setUser(useracc.selectUsersByID(proj.getUser().getId()));

			// load parent...
			if (proj.getParent() != null) {
				proj.setParent(selectProjectsByID(proj.getParent().getId()));
			}
		});

		return result;
	}

	/**
	 * Fills the projectFeatureList of the given project model.
	 * 
	 * @param project
	 *            the project to get the projectFeatureList for
	 */
	private void selectProjectsXFeatures(Project project) {
		log.debug("Getting features for the project {}", project.getId());

		jdbc.query(
				"SELECT features.id, title, description, parentid, updatedparent "
						+ "FROM projectsxfeatures JOIN features ON featureid = features.id WHERE projectid = ?",
				new Object[] { project.getId() }, new FeatureRowMapper())
				.forEach(feature -> project.addFeature(feature));
	}

	/**
	 * Fills the projectProductList of the given project model.
	 * 
	 * @param project
	 *            the project to get the projectProductList for
	 */
	private void selectProjectsXProducts(Project project) {
		log.debug("Getting products for the project {}", project.getId());

		jdbc.query(
				"SELECT products.id, title, description "
						+ "FROM projectsxproducts JOIN products ON productid = products.id WHERE projectid = ?",
				new Object[] { project.getId() }, new ProductRowMapper())
				.forEach(product -> project.addProduct(product));
	}

	/**
	 * Inserts a new project, if id is null or updates an existing one.
	 * 
	 * @param project
	 *            the project to be inserted or updated
	 * @return the updated project model
	 */
	public Project saveProject(Project project) {
		Project result = null;

		if (project.getId() == null) {
			result = insertProject(project);

			final int id = result.getId();
			result.getTrackingList().forEach(tra -> tra.setItemid(id));
		} else {
			result = updateProject(project);
		}

		// save features...
		result.getFeatureList().forEach(feat -> featacc.saveFeature(feat));

		// save products...
		result.getProductList().forEach(prod -> prodacc.saveProduct(prod));

		// save features for the project...
		saveProjectsXFeatures(result);

		// save products for the project...
		saveProjectsXProducts(result);

		// save tracking...
		trackacc.saveTracking(result.getTrackingList());

		return result;
	}

	/**
	 * Inserts a new project.
	 * 
	 * @param project
	 *            the project to be inserted
	 * @return the updated project model
	 */
	private Project insertProject(Project project) {
		KeyHolder holder = new GeneratedKeyHolder();

		jdbc.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, project.getTitle());
				ps.setString(2, project.getDescription());
				ps.setInt(3, project.getUser().getId());
				if (project.getParent() != null) {
					ps.setInt(4, project.getParent().getId());
				} else {
					ps.setNull(4, java.sql.Types.INTEGER);
				}
				ps.setBoolean(5, project.isUpdatedparent());
				ps.setTimestamp(6, new Timestamp(project.getLastChange().getTime()), Calendar.getInstance());
				return ps;
			}
		}, holder);

		int newId;
		if (holder.getKeys().size() > 1) {
			newId = (int) holder.getKeys().get("id");
		} else {
			newId = holder.getKey().intValue();
		}
		project.setId(newId);

		return project;
	}

	/**
	 * Updates the given project.
	 * 
	 * @param project
	 *            the project to be updated
	 * @return the updated project model
	 */
	private Project updateProject(Project project) {
		Integer parentid = null;

		if (project.getParent() != null) {
			parentid = project.getParent().getId();
		}

		jdbc.update(UPDATE_SQL, project.getTitle(), project.getDescription(), parentid, project.isUpdatedparent(),
				project.getLastChange(), project.getId());

		return project;
	}

	/**
	 * Inserts the features in the given projects projectFeatureList into the
	 * database.
	 * 
	 * @param project
	 *            the project whose projectFeatureList entries should be saved
	 */
	private void saveProjectsXFeatures(Project project) {
		// delete every projectxfeature connection beforehand...
		jdbc.update(DELETE_PROJECTSXFEATURES_SQL, project.getId());

		// redo connections...
		project.getFeatureList()
				.forEach(feat -> jdbc.update(INSERT_PROJECTSXFEATURES_SQL, project.getId(), feat.getId()));
	}

	/**
	 * Inserts the products in the given projects productList into the database.
	 * 
	 * @param project
	 *            the project whose productList entries should be saved
	 */
	private void saveProjectsXProducts(Project project) {
		// delete every projectxproduct connection beforehand...
		jdbc.update(DELETE_PROJECTSXPRODUCTS_SQL, project.getId());

		// redo connections...
		project.getProductList()
				.forEach(prod -> jdbc.update(INSERT_PROJECTSXPRODUCTS_SQL, project.getId(), prod.getId()));
	}

	/**
	 * Deletes the given project.
	 * 
	 * @param project
	 *            the project to be deleted
	 * @return true, if successful, false, if not
	 */
	public boolean deleteProject(Project project) {
		// delete tracking...
		trackacc.deleteTracking(project.getId(), TrackingType.PROJECT);

		// delete connections...
		jdbc.update(DELETE_PROJECTSXPRODUCTS_SQL, project.getId());
		jdbc.update(DELETE_PROJECTSXFEATURES_SQL, project.getId());

		// set childrens id to null...
		jdbc.update(UPDATE_PARENT_SQL, null, false, project.getId());

		// delete features...
		project.getFeatureList().forEach(feat -> featacc.deleteFeature(feat));

		int resultcount = jdbc.update(DELETE_SQL, project.getId());

		return resultcount > 0;
	}
}
