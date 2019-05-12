package com.swp1718.productLinRe2.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.swp1718.productLinRe2.database.helper.AssetType;
import com.swp1718.productLinRe2.database.helper.TrackingType;

/**
 * Unittest for the abstract Asset-Model and all Models witch include abstract
 * Assets
 * 
 * Last Update on 17.01.2018
 * 
 * @author Tim Gugel
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ModelAssetTest {
	// Example values to initial the tests with some example data
	private Integer ID = 800;
	private Integer ID_N = 124816;
	private String TITLE = "title";
	private String TITLE_N = "New Title";
	private String DESC = "description";
	private String DESC_N = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr.";
	private String URL = "https://some.url.de";
	private String URL_N = "https://newasset.net";
	private String METASTRING_DEFAULT = "{}";
	private String METASTRING_EMPTY = "{\"Size\":\"0B\",\"Uploaded by\":\"none\",\"Filename\":\"none\",\"FileFormat\":\"none\",\"Uploaded on\":\"none\"}";
	@SuppressWarnings("deprecation")
	private Tracking TRACK_1 = new Tracking(1, TrackingType.FEATURE, new Date(1995, 8, 15, 8, 15), "first");
	@SuppressWarnings("deprecation")
	private Tracking TRACK_2 = new Tracking(2, TrackingType.FEATURE, new Date(1995, 8, 15, 8, 28), "bugfix");

	// Example JSONObject from different values to test a possible JSON result
	private JSONObject testJSON_TID = new JSONObject();
	private JSONObject testJSON_META = new JSONObject();

	/**
	 * Initial the test-json-string
	 */
	private void initTestJSON() {
		// Initial testJSON_TID
		try {
			this.testJSON_TID.put("id", this.ID);
			this.testJSON_TID.put("title", this.TITLE);
			this.testJSON_TID.put("description", this.DESC);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// Initial testJSON_META
		try {
			testJSON_META.put("Filename", "Kroepcke.png");
			testJSON_META.put("Size", "16553000B");
			testJSON_META.put("Uploaded on", "17.12.2017");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Testing the corefeatures of model asset
	 * 
	 * @param asset
	 *            the asset-type to test
	 * @throws Exception
	 */
	private void testCorefeatures(Asset asset) throws Exception {
		// Testing exist
		assertThat(asset).isNotNull();
		// Testing getter
		assertThat(asset.toJSON().toString()).isEqualTo(this.testJSON_TID.toString());
		assertThat(asset.getId()).isEqualTo(this.ID);
		assertThat(asset.getTitle()).isEqualTo(this.TITLE);
		assertThat(asset.getDescription()).isEqualTo(this.DESC);
		assertThat(asset.getMetadata()).isEqualTo(new HashMap<String, String>());
		assertThat(asset.getMetadataAsJSONString()).isEqualTo(this.METASTRING_DEFAULT);
		// Testing AssetType
		if (asset instanceof AudioAsset) {
			assertThat(asset.getType()).isEqualTo(AssetType.AUDIO);
		} else if (asset instanceof OtherAsset) {
			assertThat(asset.getType()).isEqualTo(AssetType.OTHER);
		} else if (asset instanceof PictureAsset) {
			assertThat(asset.getType()).isEqualTo(AssetType.PICTURE);
		} else if (asset instanceof TextAsset) {
			assertThat(asset.getType()).isEqualTo(AssetType.TEXT);
		} else if (asset instanceof VideoAsset) {
			assertThat(asset.getType()).isEqualTo(AssetType.VIDEO);
		} else if (asset instanceof XMLAsset) {
			assertThat(asset.getType()).isEqualTo(AssetType.XML);
		}
		// Testing setter
		asset.setId(this.ID_N);
		assertThat(asset.getId()).isEqualTo(this.ID_N);
		asset.setTitle(this.TITLE_N);
		assertThat(asset.getTitle()).isEqualTo(this.TITLE_N);
		asset.setDescription(this.DESC_N);
		assertThat(asset.getDescription()).isEqualTo(this.DESC_N);
		asset.setURL(this.URL_N);
		assertThat(asset.getURL()).isEqualTo(this.URL_N);
		asset.setType(AssetType.PICTURE);
		assertThat(asset.getType()).isEqualTo(AssetType.PICTURE);
		asset.setMetadataWithJSONString(null);
		assertThat(asset.getMetadataAsJSONString()).isEqualTo(this.METASTRING_EMPTY);
		asset.setMetadataWithJSONString("");
		assertThat(asset.getMetadataAsJSONString()).isEqualTo(this.METASTRING_EMPTY);
		asset.setMetadataWithJSONString(this.testJSON_META.toString());
		assertThat(asset.getMetadataAsJSONString()).isEqualTo(this.testJSON_META.toString());
		// Testing Tracking
		List<Tracking> trackList = new LinkedList<Tracking>();
		assertThat(asset.getTrackingList()).isEqualTo(trackList);
		asset.addTrackingEntry(TRACK_1);
		trackList.add(TRACK_1);
		assertThat(asset.getTrackingList()).isEqualTo(trackList);
		asset.addTrackingEntry(TRACK_2);
		trackList.add(TRACK_2);
		assertThat(asset.getTrackingList()).isEqualTo(trackList);
		asset.removeTrackingEntry(TRACK_1);
		trackList.remove(TRACK_1);
		assertThat(asset.getTrackingList()).isEqualTo(trackList);
		trackList = new LinkedList<Tracking>();
		// Testing equals
		assertThat(asset.equals(asset)).isTrue();
		AudioAsset exampleAsset = new AudioAsset(this.ID, this.TITLE, this.DESC, "");
		assertThat(asset.equals(exampleAsset)).isFalse();
	}

	/**
	 * Testing the corefeatures of all asset-type classes
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAssetCorefeatures() throws Exception {
		initTestJSON();
		testCorefeatures(new AudioAsset(this.ID, this.TITLE, this.DESC, this.URL));
		testCorefeatures(new OtherAsset(this.ID, this.TITLE, this.DESC, this.URL));
		testCorefeatures(new PictureAsset(this.ID, this.TITLE, this.DESC, this.URL));
		testCorefeatures(new TextAsset(this.ID, this.TITLE, this.DESC, this.URL));
		testCorefeatures(new VideoAsset(this.ID, this.TITLE, this.DESC, this.URL));
		testCorefeatures(new XMLAsset(this.ID, this.TITLE, this.DESC, this.URL));
	}
}