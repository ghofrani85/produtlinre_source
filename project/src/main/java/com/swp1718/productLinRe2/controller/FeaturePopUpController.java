package com.swp1718.productLinRe2.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.swp1718.productLinRe2.controller.service.ProjectService;
import com.swp1718.productLinRe2.controller.service.TrackingService;
import com.swp1718.productLinRe2.controller.service.UserService;
import com.swp1718.productLinRe2.database.FeatureAccess;
import com.swp1718.productLinRe2.database.ProjectAccess;
import com.swp1718.productLinRe2.database.TrackingAccess;
import com.swp1718.productLinRe2.model.Artefact;
import com.swp1718.productLinRe2.model.Feature;
import com.swp1718.productLinRe2.model.Project;
import com.swp1718.productLinRe2.model.User;

/**
 * Handle the HTTP Requests from Copy-Feature, Edit-Feature and New-Feature
 *
 * @author Tim Gugel
 * @author Robert Völkner
 *
 */
@Controller
public class FeaturePopUpController {

	/**
	 * Redirectory to special Feature-Page
	 */
	private static final String REDIRECT_F = "redirect:feature?id=";

	/**
	 * Redirectory to special Project-Page
	 */
	private static final String REDIRECT_P = "redirect:project?id=";

	/**
	 * Database Interface for Projects.
	 */
	@Autowired
	ProjectAccess projectAccess;

	/**
	 * Database Interface for Features.
	 */
	@Autowired
	FeatureAccess featureAccess;

	/**
	 * Database Interface for Tracking.
	 */
	@Autowired
	TrackingAccess trackingAccess;

	@Autowired
	TrackingService trackingService;

	@Autowired
	ProjectService projectService;

	@Autowired
	UserService userService;

	/**
	 * Rename name, if allready exists
	 * 
	 * @param title
	 *            The new title of the Feature
	 * @param names
	 *            List of the used titles in the feature
	 * @return the renamed title
	 */
	private String renameIfExisting(String title, List<String> names) {
		String titleSuffix = "";

		int oldSuffixFirst = title.lastIndexOf('[');
		int oldSuffixLast = title.lastIndexOf(']');

		// check if the title is already a renamed version and remove the version tag
		if (oldSuffixFirst > -1 && oldSuffixLast > -1 && oldSuffixFirst < oldSuffixLast
				&& oldSuffixLast == title.length() - 1) {
			title = title.substring(0, oldSuffixFirst);
		}

		// add the correct version tag
		for (int i = 0; i <= names.size(); i++) {
			if (i != 0) {
				titleSuffix = "[" + i + "]";
			}
			if (!names.contains(title + titleSuffix)) {
				title = title + "" + titleSuffix;
				break;
			}
		}
		return title;
	}

	/**
	 * Creates a new feature and add it to a project
	 * 
	 * @param title
	 *            The new title of the Feature
	 * @param description
	 *            The new description of the Feature
	 * @param projectId
	 *            The Project for the new Feature
	 * @param httpRequest
	 *            The Http Request
	 * @return A redirect to the Project View of the current Project
	 */
	@PostMapping("/createFeature")
	public String createFeature(@RequestParam(value = "titleTextbar_create", required = true) String title,
			@RequestParam(value = "descriptionTextarea_create", required = true) String description,
			@RequestParam(value = "projId_create", required = true) Integer projectId, HttpServletRequest httpRequest) {

		// Load informations
		Project project = projectAccess.selectProjectsByID(projectId);
		List<Feature> projectFeatures = project.getFeatureList();

		// Build list with all featurenames of the project
		List<String> featureTitles = new ArrayList<>();
		for (int i = 0; i < projectFeatures.size(); i++) {
			featureTitles.add(projectFeatures.get(i).getTitle());

			// Check for DuplicateTitle
			if (projectFeatures.get(i).getTitle().equals(title)) {
				return REDIRECT_P + projectId + "&FeatureDuplicateTitleError=true&title=" + title;
			}
		}

		// Rename feature, if name allready exists
		// title = renameIfExisting(title, featureTitles); Not needed, after
		// duplicateTitleWarnings

		// Create new Feature and save
		Feature newFeature = new Feature(title, description);
		Feature savedFeature = featureAccess.saveFeature(newFeature);

		// Create tracking for that feature
		savedFeature = trackingService.trackCreatedFeature(savedFeature, httpRequest);

		// Update tracking for the project
		project = trackingService.trackAddedFeature(savedFeature, project, httpRequest);

		// Add to project and save
		project.addFeature(savedFeature);
		project.setLastChange(Calendar.getInstance().getTime());
		projectAccess.saveProject(project);

		projectService.notifyChildren(project);

		return REDIRECT_P + projectId;
	}

	/**
	 * Copy a feature from an existing project to another project with optional
	 * editing of title and description.
	 * 
	 * @param title
	 *            The new title of the Feature
	 * @param description
	 *            The new description of the Feature
	 * @param featureId
	 *            The id of the Feature that gets copied
	 * @param projectId
	 *            The id of the Project where to copy to
	 * @param httpRequest
	 *            The Http Request
	 * @return A redirect to the Feature View of the new Feature
	 */
	@PostMapping("/copyFeature")
	public String copyFeature(@RequestParam(value = "titleTextbar_copy", required = true) String title,
			@RequestParam(value = "descriptionTextarea_copy") String description,
			@RequestParam(value = "featId_copy") Integer featureId,
			@RequestParam(value = "projId_copy", required = false) Integer projectId, HttpServletRequest httpRequest) {

		if (projectId == null) {
			return REDIRECT_F + featureId;
		}

		// Load information
		Project project = projectAccess.selectProjectsByID(projectId);
		List<Feature> projectFeatures = project.getFeatureList();
		Feature feature = featureAccess.selectFeaturesByID(featureId);

		// Retrieve currently active User
		org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		User currentUser = userService.loadUser(user.getUsername());

		// Check if User is authorized
		if (!project.getUser().getId().equals(currentUser.getId()) && !currentUser.getRoles().contains("ROLE_ADMIN")) {
			return "redirect:overview";
		}

		// Build list with all featurenames of the project
		List<String> featureTitles = new ArrayList<>();
		for (int i = 0; i < projectFeatures.size(); i++) {
			featureTitles.add(projectFeatures.get(i).getTitle());
		}

		// Rename feature, if name already exists
		title = renameIfExisting(title, featureTitles);

		List<Artefact> artefactList = feature.getArtefactList();
		artefactList.forEach(art -> art.setId(null));

		// Create new feature with (maybe modified) param of the copied feature
		Feature newFeature = new Feature(null, title, description, artefactList, feature);
		Feature savedFeature = featureAccess.saveFeature(newFeature);

		artefactList = savedFeature.getArtefactList();
		List<Artefact> updatedArtefactList = new ArrayList<>();
		for (int i = 0; i < artefactList.size(); i++) {
			Artefact tempArtefact = artefactList.get(i);
			tempArtefact.setFeature(savedFeature);
			updatedArtefactList.add(tempArtefact);
		}

		savedFeature.setArtefactList(updatedArtefactList);
		savedFeature = featureAccess.saveFeature(newFeature);

		// Create tracking for copied feature from parent feature
		savedFeature = trackingService.trackCopiedFeature(feature, savedFeature, httpRequest);

		// Update tracking for the project
		project = trackingService.trackAddedFeature(savedFeature, project, httpRequest);

		// Save
		project.addFeature(savedFeature);
		project.setLastChange(Calendar.getInstance().getTime());
		projectAccess.saveProject(project);

		projectService.notifyChildren(project);

		return REDIRECT_F + savedFeature.getId();
	}

}
