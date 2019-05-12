package com.swp1718.productLinRe2.database.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.swp1718.productLinRe2.model.Project;
import com.swp1718.productLinRe2.model.User;

/**
 * RowMapper for the project model.
 * 
 * @author Tobias Powelske
 *
 */
public class ProjectRowMapper implements RowMapper<Project> {

	@Override
	public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
		Project project = new Project();

		project.setId(rs.getInt("id"));
		project.setTitle(rs.getString("title"));
		project.setDescription(rs.getString("description"));
		project.setUser(new User(rs.getInt("userid"), null, null, null, false, false, null, null, null, null));
		project.setParent(new Project(rs.getInt("parentid"), null, null, null, null, null, null, null, false));
		project.setUpdatedparent(rs.getBoolean("updatedparent"));
		project.setLastChange(rs.getTimestamp("last_change"));

		return project;
	}

}
