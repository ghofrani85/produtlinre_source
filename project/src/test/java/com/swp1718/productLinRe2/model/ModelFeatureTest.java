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
 * Unittest for the Feature-Model
 * 
 * Last Update on 17.01.2018
 * 
 * @author Tim Gugel
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ModelFeatureTest {
	// Example values to initial the tests with some data
	private Integer ID = 800;
	private Integer ID_N = 124816;
	private String TITLE = "title";
	private String TITLE_N = "New Title";
	private String DESC = "description";
	private String DESC_N = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr.";
	private Feature PARENT = new Feature();
	@SuppressWarnings("deprecation")
	private Tracking TRACK_1 = new Tracking(1, TrackingType.ARTEFACT, new Date(1995, 8, 15, 8, 15), "first");
	@SuppressWarnings("deprecation")
	private Tracking TRACK_2 = new Tracking(2, TrackingType.USER, new Date(1995, 8, 15, 8, 28), "bugfix");
	private Artefact ARTE_1 = new AudioArtefact(1, "title 1",
			new AudioAsset(this.ID, this.TITLE, "Lorem ipsum", "https:uiorweh"), "", "");
	private Artefact ARTE_2 = new XMLArtefact(2, "title 2",
			new XMLAsset(this.ID, this.TITLE, "description", "www.254nfio23.com/wrf"), "");

	// Example values witch needs to be initial with initTestData()
	private JSONObject testJSON = new JSONObject();
	private List<Artefact> arteList = new ArrayList<Artefact>();
	private List<Artefact> arteList_n = new ArrayList<Artefact>();

	/**
	 * Initial the test-json-data and the artefact-lists
	 */
	private void initTestData() {
		this.arteList.add(new AudioArtefact(1, "Title", new AudioAsset(1, "Title", "Desc", "One"), "", ""));
		this.arteList.add(new XMLArtefact(2, "Title", new XMLAsset(11, "Title", "Desc", "Two"), ""));
		this.arteList_n.add(new AudioArtefact(1, "Title", new AudioAsset(1, "Title", "Desc", "Three"), "", ""));
		this.arteList_n.add(new XMLArtefact(2, "Title", new XMLAsset(11, "Title", "Desc", "Four"), ""));
		try {
			this.testJSON.put("id", this.ID);
			this.testJSON.put("title", this.TITLE);
			this.testJSON.put("description", this.DESC);
			this.testJSON.put("assetList", this.arteList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Testing the three constructors of feature
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFeatureConstructors() throws Exception {
		// Testing default contructor
		Feature feature = new Feature();
		assertThat(feature.getId()).isNull();
		assertThat(feature.getTitle()).isEqualTo("");
		assertThat(feature.getDescription()).isEqualTo("");
		assertThat(feature.getArtefactList()).isEqualTo(new ArrayList<Artefact>());
		assertThat(feature.getParent()).isNull();
		assertThat(feature.isUpdatedparent()).isFalse();
		assertThat(feature.getTrackingList()).isEqualTo(new LinkedList<Tracking>());
		// Testing first constructor
		feature = new Feature(this.TITLE, this.DESC);
		assertThat(feature.getId()).isNull();
		assertThat(feature.getTitle()).isEqualTo(this.TITLE);
		assertThat(feature.getDescription()).isEqualTo(this.DESC);
		assertThat(feature.getArtefactList()).isEqualTo(new ArrayList<Artefact>());
		assertThat(feature.getParent()).isNull();
		assertThat(feature.isUpdatedparent()).isFalse();
		assertThat(feature.getTrackingList()).isEqualTo(new LinkedList<Tracking>());
		// Testing secound constructor
		List<Artefact> arteList = new ArrayList<Artefact>();
		arteList.add(new AudioArtefact(1, "Title", new AudioAsset(1, "Title", "Desc", "url"), "", ""));
		arteList.add(new XMLArtefact(2, "Title", new XMLAsset(11, "Title", "Desc", "url"), ""));
		feature = new Feature(this.ID, this.TITLE, this.DESC, arteList);
		assertThat(feature.getId()).isEqualTo(this.ID);
		assertThat(feature.getTitle()).isEqualTo(this.TITLE);
		assertThat(feature.getDescription()).isEqualTo(this.DESC);
		assertThat(feature.getArtefactList()).isEqualTo(arteList);
		assertThat(feature.getParent()).isNull();
		assertThat(feature.isUpdatedparent()).isFalse();
		assertThat(feature.getTrackingList()).isEqualTo(new LinkedList<Tracking>());
	}

	/**
	 * Testing the corefeatures of Feature Model
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFeatureCorefeatures() throws Exception {
		initTestData();
		// Build new Feature
		Feature feature = new Feature(this.ID, this.TITLE, this.DESC, arteList);
		// Testing exist
		assertThat(feature).isNotNull();
		// Testing getter
		assertThat(feature.getId()).isEqualTo(this.ID);
		assertThat(feature.getTitle()).isEqualTo(this.TITLE);
		assertThat(feature.getDescription()).isEqualTo(this.DESC);
		assertThat(feature.getArtefactList()).isEqualTo(arteList);
		assertThat(feature.getParent()).isNull();
		assertThat(feature.isUpdatedparent()).isFalse();
		assertThat(feature.getTrackingList()).isEqualTo(new LinkedList<Tracking>());
		// Testing setter
		feature.setId(this.ID_N);
		feature.setTitle(this.TITLE_N);
		feature.setDescription(this.DESC_N);
		feature.setParent(this.PARENT);
		assertThat(feature.getId()).isEqualTo(this.ID_N);
		assertThat(feature.getTitle()).isEqualTo(this.TITLE_N);
		assertThat(feature.getDescription()).isEqualTo(this.DESC_N);
		assertThat(feature.getParent()).isEqualTo(this.PARENT);
		// Testing Tracking
		List<Tracking> trackList = new LinkedList<Tracking>();
		assertThat(feature.getTrackingList()).isEqualTo(trackList);
		feature.addTrackingEntry(TRACK_1);
		trackList.add(TRACK_1);
		assertThat(feature.getTrackingList()).isEqualTo(trackList);
		feature.addTrackingEntry(TRACK_2);
		trackList.add(TRACK_2);
		assertThat(feature.getTrackingList()).isEqualTo(trackList);
		feature.removeTrackingEntry(TRACK_1);
		trackList.remove(TRACK_1);
		assertThat(feature.getTrackingList()).isEqualTo(trackList);
		// Testing ArtefactList
		feature.setArtefactList(new LinkedList<Artefact>());
		List<Artefact> artefactList = new LinkedList<Artefact>();
		assertThat(feature.getArtefactList()).isEqualTo(artefactList);
		feature.addArtefact(ARTE_1);
		artefactList.add(ARTE_1);
		assertThat(feature.getArtefactList()).isEqualTo(artefactList);
		feature.addArtefact(ARTE_2);
		artefactList.add(ARTE_2);
		assertThat(feature.getArtefactList()).isEqualTo(artefactList);
		feature.removeArtefact(ARTE_1);
		artefactList.remove(ARTE_1);
		assertThat(feature.getArtefactList()).isEqualTo(artefactList);
		// Testing equals
		assertThat(feature.equals(feature)).isTrue();
		assertThat(feature.equals(new Feature("t21", "d17"))).isFalse();
	}

}