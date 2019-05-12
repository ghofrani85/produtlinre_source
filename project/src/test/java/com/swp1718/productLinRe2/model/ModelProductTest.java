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
 * Unittest for the Product-Model
 * 
 * Last Update on 17.01.2018
 * 
 * @author Tim Gugel
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ModelProductTest {
	// Example values to initial the tests with some data
	private Integer ID = 2;
	private Integer ID_N = 124816;
	private String TITLE = "title";
	private String TITLE_N = "New Title";
	private String DESC = "description";
	private String DESC_N = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr.";
	@SuppressWarnings("deprecation")
	private Tracking TRACK_1 = new Tracking(1, TrackingType.FEATURE, new Date(1995, 8, 15, 8, 15), "first");
	@SuppressWarnings("deprecation")
	private Tracking TRACK_2 = new Tracking(2, TrackingType.ASSET, new Date(1995, 8, 15, 8, 28), "bugfix");
	private Feature FEAT_1 = new Feature("Title 1", "Des 1");
	private Feature FEAT_2 = new Feature("T2,23", "Des 002");

	// Example values witch needs to be initial with initTestData()
	private JSONObject testJSON = new JSONObject();
	private List<Feature> featList = new ArrayList<Feature>();
	private List<Feature> featList_n = new ArrayList<Feature>();

	/**
	 * Initial the test-json-data and the feature-lists
	 */
	private void initTestData() {
		this.featList.add(new Feature("Some", "Name"));
		this.featList.add(new Feature("And", "More"));
		this.featList_n.add(new Feature("The", "Next"));
		this.featList_n.add(new Feature("Will", "End"));
		try {
			this.testJSON.put("id", this.ID);
			this.testJSON.put("title", this.TITLE);
			this.testJSON.put("description", this.DESC);
			this.testJSON.put("features", this.featList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Testing the four constructors of Product
	 * 
	 * @throws Exception
	 */
	@Test
	public void testProductConstructors() throws Exception {
		// Testing default contructor
		Product product = new Product();
		assertThat(product.getId()).isNull();
		assertThat(product.getTitle()).isEqualTo("");
		assertThat(product.getDescription()).isEqualTo("");
		assertThat(product.getFeatureList()).isEqualTo(new ArrayList<Feature>());
		// Testing constructor param 2
		product = new Product(this.TITLE, this.DESC);
		assertThat(product.getId()).isNull();
		assertThat(product.getTitle()).isEqualTo(this.TITLE);
		assertThat(product.getDescription()).isEqualTo(this.DESC);
		assertThat(product.getFeatureList()).isEqualTo(new ArrayList<Feature>());
		// Testing constructor param 3
		product = new Product(this.ID, this.TITLE, this.DESC);
		assertThat(product.getId()).isEqualTo(this.ID);
		assertThat(product.getTitle()).isEqualTo(this.TITLE);
		assertThat(product.getDescription()).isEqualTo(this.DESC);
		assertThat(product.getFeatureList()).isEqualTo(new ArrayList<Feature>());
		// Testing constructor param 4
		List<Feature> featList = new ArrayList<Feature>();
		featList.add(new Feature("Some", "Name"));
		featList.add(new Feature("And", "More"));
		product = new Product(this.ID, this.TITLE, this.DESC, featList);
		assertThat(product.getId()).isEqualTo(this.ID);
		assertThat(product.getTitle()).isEqualTo(this.TITLE);
		assertThat(product.getDescription()).isEqualTo(this.DESC);
		assertThat(product.getFeatureList()).isEqualTo(featList);
	}

	/**
	 * Testing the corefeatures of Product
	 * 
	 * @throws Exception
	 */
	@Test
	public void testProductCorefeatures() throws Exception {
		initTestData();
		// Build new Product
		Product product = new Product(this.ID, this.TITLE, this.DESC, this.featList);
		// Testing exist
		assertThat(product).isNotNull();
		// Testing getter
		assertThat(product.getId()).isEqualTo(this.ID);
		assertThat(product.getTitle()).isEqualTo(this.TITLE);
		assertThat(product.getDescription()).isEqualTo(this.DESC);
		assertThat(product.getFeatureList()).isEqualTo(this.featList);
		// Testing setter
		product.setId(this.ID_N);
		product.setTitle(this.TITLE_N);
		product.setDescription(this.DESC_N);
		assertThat(product.getId()).isEqualTo(this.ID_N);
		assertThat(product.getTitle()).isEqualTo(this.TITLE_N);
		assertThat(product.getDescription()).isEqualTo(this.DESC_N);
		// Testing Tracking
		List<Tracking> trackList = new LinkedList<Tracking>();
		assertThat(product.getTrackingList()).isEqualTo(trackList);
		product.addTrackingEntry(TRACK_1);
		trackList.add(TRACK_1);
		assertThat(product.getTrackingList()).isEqualTo(trackList);
		product.addTrackingEntry(TRACK_2);
		trackList.add(TRACK_2);
		assertThat(product.getTrackingList()).isEqualTo(trackList);
		product.removeTrackingEntry(TRACK_1);
		trackList.remove(TRACK_1);
		assertThat(product.getTrackingList()).isEqualTo(trackList);
		// Testing FeatureList
		product.setFeatureList(new LinkedList<Feature>());
		List<Feature> featureList = new LinkedList<Feature>();
		assertThat(product.getFeatureList()).isEqualTo(featureList);
		product.addFeature(FEAT_1);
		featureList.add(FEAT_1);
		assertThat(product.getFeatureList()).isEqualTo(featureList);
		product.addFeature(FEAT_2);
		featureList.add(FEAT_2);
		assertThat(product.getFeatureList()).isEqualTo(featureList);
		product.setFeatureList(featureList);
		assertThat(product.getFeatureList()).isEqualTo(featureList);
		product.removeFeature(FEAT_1);
		List<String> featNames = new ArrayList<String>();
		for (Feature f : featureList) {
			featNames.add(f.getTitle());
		}
		assertThat(product.getFeatureNames()).isEqualTo(featNames);
		featureList.remove(FEAT_1);
		assertThat(product.getFeatureList()).isEqualTo(featureList);
		// Testing equals
		assertThat(product.equals(product)).isTrue();
		assertThat(product.equals(new Product("t21", "d17"))).isFalse();
	}

}