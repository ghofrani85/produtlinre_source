package com.swp1718.productLinRe2.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.swp1718.productLinRe2.database.helper.TrackingType;

/**
 * Unittest for the abstract Artefact-Model and all Models witch include
 * abstract Artefact
 * 
 * Last Update on 17.01.2018
 * 
 * @author Tim Gugel
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ModelArtefactTest {
	// Example values to initial the tests with some data
	private Integer ID = 800;
	private Integer ID_N = 124816;
	private String TITLE = "title";
	private String TITLE_N = "New Title";
	private Asset ASSET = new AudioAsset(this.ID, this.TITLE, "Lorem ipsum", "https://url.net");
	private Asset ASSET_N = new XMLAsset(this.ID, this.TITLE, "description", "https://XMLAsset.path.wfgb.com/de");
	@SuppressWarnings("deprecation")
	private Tracking TRACK_1 = new Tracking(1, TrackingType.ARTEFACT, new Date(1995, 8, 15, 8, 15), "first");
	@SuppressWarnings("deprecation")
	private Tracking TRACK_2 = new Tracking(2, TrackingType.ASSET, new Date(1995, 8, 15, 8, 28), "bugfix");

	/**
	 * Testing the corefeatures of a artefact witch are included in the Artefact
	 * Abstract.
	 * 
	 * @param artefact
	 *            Artefact to test
	 * @throws Exception
	 */
	private void testCorefeatures(Artefact artefact) throws Exception {
		// Testing exist
		assertThat(artefact).isNotNull();
		// Testing getter
		assertThat(artefact.getId()).isEqualTo(this.ID);
		assertThat(artefact.getTitle()).isEqualTo(this.TITLE);
		assertThat(artefact.getAsset()).isEqualTo(this.ASSET);
		assertThat(artefact.getFeature()).isNull();
		// Testing setter
		artefact.setId(this.ID_N);
		assertThat(artefact.getId()).isEqualTo(this.ID_N);
		artefact.setTitle(this.TITLE_N);
		assertThat(artefact.getTitle()).isEqualTo(this.TITLE_N);
		artefact.setAsset(this.ASSET_N);
		assertThat(artefact.getAsset()).isEqualTo(this.ASSET_N);
		artefact.setFeature(new Feature());
		assertThat(artefact.getFeature()).isNotEqualTo(new Feature());
		// Testing Tracking
		List<Tracking> trackList = new LinkedList<Tracking>();
		assertThat(artefact.getTrackingList()).isEqualTo(trackList);
		artefact.addTrackingEntry(TRACK_1);
		trackList.add(TRACK_1);
		assertThat(artefact.getTrackingList()).isEqualTo(trackList);
		artefact.addTrackingEntry(TRACK_2);
		trackList.add(TRACK_2);
		assertThat(artefact.getTrackingList()).isEqualTo(trackList);
		artefact.removeTrackingEntry(TRACK_1);
		trackList.remove(TRACK_1);
		assertThat(artefact.getTrackingList()).isEqualTo(trackList);
		trackList = new LinkedList<Tracking>();
		// Testing equals
		assertThat(artefact.equals(artefact)).isTrue();
		AudioArtefact exampleArtefact = new AudioArtefact(this.ID, this.TITLE, this.ASSET, "", "");
		assertThat(artefact.equals(exampleArtefact)).isFalse();
	}

	/**
	 * Testing the corefeatures of all models witch include abstract Artefact
	 * 
	 * @throws Exception
	 */
	@Test
	public void testArtefactCorefeatures() throws Exception {
		testCorefeatures(new AudioArtefact(this.ID, this.TITLE, this.ASSET, "", "")); // AudioArtefact
		testCorefeatures(new OtherArtefact(this.ID, this.TITLE, this.ASSET, "", "")); // OtherArtefact
		testCorefeatures(new PictureArtefact(this.ID, this.TITLE, this.ASSET, "", "", "", "")); // PictureArtefact
		testCorefeatures(new TextArtefact(this.ID, this.TITLE, this.ASSET, "", "")); // TextArtefact
		testCorefeatures(new VideoArtefact(this.ID, this.TITLE, this.ASSET, "", "")); // VideoArtefact
		testCorefeatures(new XMLArtefact(this.ID, this.TITLE, this.ASSET, "")); // XMLArtefact
	}

	/**
	 * Testing specialfeatures of AudioArtefact Model
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAudioArtefact() throws Exception {
		AudioArtefact artefact = new AudioArtefact(this.ID, this.TITLE, this.ASSET, "20", "120");
		assertThat(artefact).isNotNull();
		// Testing getter
		assertThat(artefact.getStart()).isEqualTo("20");
		assertThat(artefact.getEnd()).isEqualTo("120");
		// Testing setter
		artefact.setStart("15");
		assertThat(artefact.getStart()).isEqualTo("15");
		artefact.setEnd("100");
		assertThat(artefact.getEnd()).isEqualTo("100");
		// Testing equals
		AudioArtefact a0 = new AudioArtefact(this.ID, this.TITLE, this.ASSET, "20", "120");
		AudioArtefact a1 = new AudioArtefact(this.ID_N, this.TITLE, this.ASSET, "20", "120");
		assertThat(a0.equals(a0)).isTrue();
		assertThat(a0.equals(a1)).isFalse();
	}

	/**
	 * Testing specialfeatures of OtherArtefact Model
	 * 
	 * @throws Exception
	 */
	@Test
	public void testOtherArtefact() throws Exception {
		OtherArtefact artefact = new OtherArtefact(this.ID, this.TITLE, this.ASSET, "20", "120");
		// Testing constructor
		assertThat(artefact).isNotNull();
		// Testing getter
		assertThat(artefact.getStart()).isEqualTo("20");
		assertThat(artefact.getEnd()).isEqualTo("120");
		// Testing setter
		artefact.setStart("15");
		artefact.setEnd("100");
		assertThat(artefact.getStart()).isEqualTo("15");
		assertThat(artefact.getEnd()).isEqualTo("100");
		// Testing equals
		OtherArtefact a0 = new OtherArtefact(this.ID, this.TITLE, this.ASSET, "20", "120");
		OtherArtefact a1 = new OtherArtefact(this.ID_N, this.TITLE, this.ASSET, "20", "120");
		assertThat(a0.equals(a0)).isTrue();
		assertThat(a0.equals(a1)).isFalse();
	}

	/**
	 * Testing specialfeatures of PictureArtefact Model
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPictureArtefact() throws Exception {
		PictureArtefact artefact = new PictureArtefact(this.ID, this.TITLE, this.ASSET, "100", "200", "300", "400");
		assertThat(artefact).isNotNull();
		// Testing getter
		assertThat(artefact.getX()).isEqualTo("100");
		assertThat(artefact.getY()).isEqualTo("200");
		assertThat(artefact.getWidth()).isEqualTo("300");
		assertThat(artefact.getHeight()).isEqualTo("400");
		// Testing setter
		artefact.setX("11");
		artefact.setY("22");
		artefact.setWidth("33");
		artefact.setHeight("44");
		assertThat(artefact.getX()).isEqualTo("11");
		assertThat(artefact.getY()).isEqualTo("22");
		assertThat(artefact.getWidth()).isEqualTo("33");
		assertThat(artefact.getHeight()).isEqualTo("44");
		// Testing equals
		PictureArtefact a0 = new PictureArtefact(this.ID, this.TITLE, this.ASSET, "100", "200", "300", "400");
		PictureArtefact a1 = new PictureArtefact(this.ID_N, this.TITLE, this.ASSET, "100", "200", "300", "400");
		assertThat(a0.equals(a0)).isTrue();
		assertThat(a0.equals(a1)).isFalse();
	}

	/**
	 * Testing specialfeatures of TextArtefact Model
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTextArtefact() throws Exception {
		TextArtefact artefact = new TextArtefact(this.ID, this.TITLE, this.ASSET, "20", "120");
		assertThat(artefact).isNotNull();
		// Testing getter
		assertThat(artefact.getStart()).isEqualTo("20");
		assertThat(artefact.getEnd()).isEqualTo("120");
		// Testing setter
		artefact.setStart("15");
		artefact.setEnd("100");
		assertThat(artefact.getStart()).isEqualTo("15");
		assertThat(artefact.getEnd()).isEqualTo("100");
		// Testing equals
		TextArtefact a0 = new TextArtefact(this.ID, this.TITLE, this.ASSET, "20", "120");
		TextArtefact a1 = new TextArtefact(this.ID_N, this.TITLE, this.ASSET, "20", "120");
		assertThat(a0.equals(a0)).isTrue();
		assertThat(a0.equals(a1)).isFalse();
	}

	/**
	 * Testing specialfeatures of VideoArtefact Model
	 * 
	 * @throws Exception
	 */
	@Test
	public void testVideoArtefact() throws Exception {
		VideoArtefact artefact = new VideoArtefact(this.ID, this.TITLE, this.ASSET, "20", "120");
		assertThat(artefact).isNotNull();
		// Testing getter
		assertThat(artefact.getStart()).isEqualTo("20");
		assertThat(artefact.getEnd()).isEqualTo("120");
		// Testing setter
		artefact.setStart("15");
		artefact.setEnd("100");
		assertThat(artefact.getStart()).isEqualTo("15");
		assertThat(artefact.getEnd()).isEqualTo("100");
		// Testing equals
		VideoArtefact a0 = new VideoArtefact(this.ID, this.TITLE, this.ASSET, "20", "120");
		VideoArtefact a1 = new VideoArtefact(this.ID_N, this.TITLE, this.ASSET, "20", "120");
		assertThat(a0.equals(a0)).isTrue();
		assertThat(a0.equals(a1)).isFalse();
	}

	/**
	 * Testing specialfeatures of XMLArtefact Model
	 * 
	 * @throws Exception
	 */
	@Test
	public void testXMLArtefact() throws Exception {
		XMLArtefact artefact = new XMLArtefact(this.ID, this.TITLE, this.ASSET, "20");
		assertThat(artefact).isNotNull();
		// Testing getter
		assertThat(artefact.getNodes()).isEqualTo("20");
		// Testing setter
		artefact.setNodes("15");
		assertThat(artefact.getNodes()).isEqualTo("15");
		// Testing equals
		XMLArtefact a0 = new XMLArtefact(this.ID, this.TITLE, this.ASSET, "20");
		XMLArtefact a1 = new XMLArtefact(this.ID_N, this.TITLE, this.ASSET, "20");
		assertThat(a0.equals(a0)).isTrue();
		assertThat(a0.equals(a1)).isFalse();
	}
}