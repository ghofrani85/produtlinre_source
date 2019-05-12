package com.swp1718.productLinRe2.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Simple test that checks if the application context starts and that the view
 * controllers are created.
 * 
 * @author Daniel Wahlmann
 * @author Tim Gugel
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

	// Add controllers as they are implemented
	@Autowired
	private AdminViewController adminViewController;

	@Autowired
	private AssetController assetController;

	@Autowired
	private AudioArtefactController audioArtefactController;

	@Autowired
	private BrowseController browseController;

	@Autowired
	private CheckFeatureController checkFeatureController;

	@Autowired
	private DownloadProductController downloadProductController;

	@Autowired
	private FeatureController featureController;

	@Autowired
	private FeaturePopUpController featurePopUpController;

	@Autowired
	private FileController fileController;

	@Autowired
	private ForgotPasswordController forgotPasswordController;

	@Autowired
	private GenerateZipController generateZipController;

	@Autowired
	private OverviewController overviewController;

	@Autowired
	private PictureArtefactController pictureArtefactController;

	@Autowired
	private ProjectController projectController;

	@Autowired
	private RegistrationController registrationController;

	@Autowired
	private SecurityAccess securityAccess;

	@Autowired
	private TextArtefactController textArtefactController;

	@Autowired
	private UserSettingsController userSettingsController;

	@Autowired
	private VideoArtefactController videoArtefactController;

	@Autowired
	private XMLArtefactController xmlArtefactController;

	/**
	 * Simple sanity check.
	 * 
	 * @throws Exception
	 */
	@Test
	public void contextLoads() throws Exception {
		assertThat(adminViewController).isNotNull();
		assertThat(assetController).isNotNull();
		assertThat(audioArtefactController).isNotNull();
		assertThat(browseController).isNotNull();
		assertThat(checkFeatureController).isNotNull();
		assertThat(downloadProductController).isNotNull();
		assertThat(featureController).isNotNull();
		assertThat(featurePopUpController).isNotNull();
		assertThat(fileController).isNotNull();
		assertThat(forgotPasswordController).isNotNull();
		assertThat(generateZipController).isNotNull();
		assertThat(overviewController).isNotNull();
		assertThat(pictureArtefactController).isNotNull();
		assertThat(projectController).isNotNull();
		assertThat(registrationController).isNotNull();
		assertThat(securityAccess).isNotNull();
		assertThat(textArtefactController).isNotNull();
		assertThat(userSettingsController).isNotNull();
		assertThat(videoArtefactController).isNotNull();
		assertThat(xmlArtefactController).isNotNull();
	}
}