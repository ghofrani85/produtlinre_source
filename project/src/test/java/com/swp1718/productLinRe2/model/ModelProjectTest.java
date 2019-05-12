package com.swp1718.productLinRe2.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.swp1718.productLinRe2.database.helper.TrackingType;

/**
 * Unittest for the Project-Model
 * 
 * Last Update on 17.01.2018
 * 
 * @author Tim Gugel
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ModelProjectTest {
	// Example values to initial the tests with some data
	private Integer ID = 2;
	private Integer ID_N = 4;
	private String TITLE = "Title";
	private String TITLE_N = "New Title";
	private String DESC = "Description";
	private String DESC_N = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr.";
	@SuppressWarnings("deprecation")
	private Date DATE = new Date(2017, 12, 4, 2, 8);
	@SuppressWarnings("deprecation")
	private Date DATE_N = new Date(2017, 12, 5, 1, 10);
	private User USER = new User();
	private User USER_N = new User();
	private Feature FEAT_1 = new Feature("Feature One", "Description 1");
	private Feature FEAT_2 = new Feature("Feature Two", "One Two Two");
	@SuppressWarnings("deprecation")
	private Tracking TRACK_1 = new Tracking(1, TrackingType.ARTEFACT, new Date(1995, 8, 15, 8, 15), "first");
	@SuppressWarnings("deprecation")
	private Tracking TRACK_2 = new Tracking(2, TrackingType.FEATURE, new Date(1995, 8, 15, 8, 28), "bugfix");

	// Example values witch needs to be initial with initTestData()
	private JSONObject testJSON = new JSONObject();
	private List<Feature> featList = new ArrayList<Feature>();
	private List<Feature> featList_n = new ArrayList<Feature>();
	private List<Product> prodList = new ArrayList<Product>();
	private List<Product> prodList_n = new ArrayList<Product>();

	/**
	 * Initial the test-json-data and the feature- and the product-lists
	 */
	private void initTestData() {
		testJSON = new JSONObject();
		featList = new ArrayList<Feature>();
		featList_n = new ArrayList<Feature>();
		prodList = new ArrayList<Product>();
		prodList_n = new ArrayList<Product>();
		this.featList.add(new Feature("Feat 01", "A feature called 01"));
		this.featList.add(new Feature("Feat 02", "A feature called 02"));
		this.featList_n.add(new Feature("New Feat 01", "The new feature called 01"));
		this.featList_n.add(new Feature("New Feat 02", "The new feature called 02"));
		this.prodList.add(new Product("Product A", "Bliblablo"));
		this.prodList.add(new Product("Product B", "More desc..."));
		this.prodList_n.add(new Product("New Prod-A", "Some new text"));
		this.prodList_n.add(new Product("New Prod-B", "A product called B"));

		try {
			this.testJSON.put("id", this.ID);
			this.testJSON.put("title", this.TITLE);
			this.testJSON.put("description", this.DESC);
			this.testJSON.put("lastChange", this.DATE);
			this.testJSON.put("productList", this.prodList);
			this.testJSON.put("projectFeatureList", this.featList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Testing the three constructors of Product
	 * 
	 * @throws Exception
	 */
	@Test
	public void testProjectConstructors() throws Exception {
		initTestData();
		// Testing default constructor
		Project project = new Project();
		assertThat(project.getId()).isNull();
		assertThat(project.getTitle()).isEqualTo("");
		assertThat(project.getDescription()).isEqualTo("");
		assertThat(project.getProductList()).isEqualTo(new LinkedList<Product>());
		assertThat(project.getFeatureList()).isEqualTo(new LinkedList<Feature>());
		assertThat(project.getUser()).isNull();
		// Testing constructor param 2
		project = new Project(this.TITLE, this.DESC, this.USER);
		assertThat(project.getId()).isNull();
		assertThat(project.getTitle()).isEqualTo(this.TITLE);
		assertThat(project.getDescription()).isEqualTo(this.DESC);
		assertThat(project.getProductList()).isEqualTo(new LinkedList<Product>());
		assertThat(project.getFeatureList()).isEqualTo(new LinkedList<Feature>());
		assertThat(project.getUser()).isEqualTo(this.USER);
		// Testing constructor param max
		project = new Project(this.ID, this.TITLE, this.DESC, this.DATE, this.prodList, this.featList, this.USER, null,
				false);
		assertThat(project.getId()).isEqualTo(this.ID);
		assertThat(project.getTitle()).isEqualTo(this.TITLE);
		assertThat(project.getDescription()).isEqualTo(this.DESC);
		assertThat(project.getLastChange()).isEqualTo(this.DATE);
		assertThat(project.getProductList()).isEqualTo(this.prodList);
		assertThat(project.getFeatureList()).isEqualTo(this.featList);
		assertThat(project.getUser()).isEqualTo(this.USER);
	}

	/**
	 * Testing the corefeatures of Project Model
	 * 
	 * @throws Exception
	 */
	@Test
	public void testProjectCorefeatures() throws Exception {
		initTestData();
		// Build new Product
		Project project = new Project(this.ID, this.TITLE, this.DESC, this.DATE, this.prodList, this.featList,
				this.USER, null, false);
		// Testing exist
		assertThat(project).isNotNull();
		// Testing getter
		assertThat(project.getId()).isEqualTo(this.ID);
		assertThat(project.getTitle()).isEqualTo(this.TITLE);
		assertThat(project.getDescription()).isEqualTo(this.DESC);
		assertThat(project.getLastChange()).isEqualTo(this.DATE);
		assertThat(project.getProductList()).isEqualTo(this.prodList);
		assertThat(project.getFeatureList()).isEqualTo(this.featList);
		assertThat(project.getUser()).isEqualTo(this.USER);
		// Testing setter
		project.setId(this.ID_N);
		project.setTitle(this.TITLE_N);
		project.setDescription(this.DESC_N);
		project.setLastChange(this.DATE_N);
		project.setProductList(this.prodList_n);
		project.setFeatureList(this.featList_n);
		project.setUser(this.USER_N);
		assertThat(project.getId()).isEqualTo(this.ID_N);
		assertThat(project.getTitle()).isEqualTo(this.TITLE_N);
		assertThat(project.getDescription()).isEqualTo(this.DESC_N);
		assertThat(project.getLastChange()).isEqualTo(this.DATE_N);
		assertThat(project.getProductList()).isEqualTo(this.prodList_n);
		assertThat(project.getFeatureList()).isEqualTo(this.featList_n);
		assertThat(project.getUser()).isEqualTo(this.USER_N);
		// Testing Tracking
		List<Tracking> trackList = new LinkedList<Tracking>();
		assertThat(project.getTrackingList()).isEqualTo(trackList);
		project.addTrackingEntry(TRACK_1);
		trackList.add(TRACK_1);
		assertThat(project.getTrackingList()).isEqualTo(trackList);
		project.addTrackingEntry(TRACK_2);
		trackList.add(TRACK_2);
		assertThat(project.getTrackingList()).isEqualTo(trackList);
		project.removeTrackingEntry(TRACK_1);
		trackList.remove(TRACK_1);
		assertThat(project.getTrackingList()).isEqualTo(trackList);
		// Testing FeatureList/ProjectFeatureList
		project.setFeatureList(new LinkedList<Feature>());
		List<Feature> featureList = new LinkedList<Feature>();
		assertThat(project.getFeatureList()).isEqualTo(featureList);
		project.addFeature(FEAT_1);
		featureList.add(FEAT_1);
		assertThat(project.getFeatureList()).isEqualTo(featureList);
		project.addFeature(FEAT_2);
		featureList.add(FEAT_2);
		assertThat(project.getFeatureList()).isEqualTo(featureList);
		project.removeFeature(FEAT_1);
		featureList.remove(FEAT_1);
		assertThat(project.getFeatureList()).isEqualTo(featureList);
		// Testing equals
		assertThat(project.equals(project)).isTrue();
		assertThat(project.equals(new Project("t21", "d17", new User()))).isFalse();
	}
}