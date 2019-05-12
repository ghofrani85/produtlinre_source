package com.swp1718.productLinRe2.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.swp1718.productLinRe2.database.helper.TrackingType;

/**
 * Unittest for the Tracking-Model
 * 
 * Last Update on 17.01.2018
 * 
 * @author Tim Gugel
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ModelTrackingTest {
	// Example values to init the tests with some data
	private Integer ID = 1;
	private Integer ID_N = 8;
	@SuppressWarnings("deprecation")
	private Date DATE = new Date(1995, 8, 15, 8, 15);
	@SuppressWarnings("deprecation")
	private Date DATE_N = new Date(2010, 10, 15, 4, 8);
	private String TEXT = "Some words";
	private String TEXT_N = "Some new words without content";
	private TrackingType TYPE = TrackingType.ARTEFACT;
	private TrackingType TYPE_N = TrackingType.USER;

	/**
	 * Testing of the constructor
	 * 
	 * @throws Exception
	 */
	@Test
	public void constructorTest() throws Exception {
		// Testing default constructor
		Tracking track = new Tracking();
		assertThat(track.getItemid()).isNull();
		assertThat(track.getText()).isNull();
		assertThat(track.getChangemade()).isNull();
		assertThat(track.getType()).isNull();
		// Testing constructor
		track = new Tracking(this.ID, this.TYPE, this.DATE, this.TEXT);
		assertThat(track.getItemid()).isEqualTo(this.ID);
		assertThat(track.getText()).isEqualTo(this.TEXT);
		assertThat(track.getChangemade()).isEqualTo(this.DATE);
		assertThat(track.getType()).isEqualTo(this.TYPE);
	}

	/**
	 * Testing the corefeatures of Tracking Model
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTrackingCorefeatures() throws Exception {
		// Build new Product
		Tracking track = new Tracking(this.ID, this.TYPE, this.DATE, this.TEXT);
		// Testing exist
		assertThat(track).isNotNull();
		// Testing getter
		assertThat(track.getItemid()).isEqualTo(this.ID);
		assertThat(track.getText()).isEqualTo(this.TEXT);
		assertThat(track.getChangemade()).isEqualTo(this.DATE);
		assertThat(track.getType()).isEqualTo(this.TYPE);
		// Testing setter
		track.setItemid(this.ID_N);
		track.setText(this.TEXT_N);
		track.setChangemade(this.DATE_N);
		track.setType(this.TYPE_N);
		assertThat(track.getItemid()).isEqualTo(this.ID_N);
		assertThat(track.getText()).isEqualTo(this.TEXT_N);
		assertThat(track.getChangemade()).isEqualTo(this.DATE_N);
		assertThat(track.getType()).isEqualTo(this.TYPE_N);
		// Testing equals
		assertThat(track.equals(track)).isTrue();
		Tracking track2 = new Tracking(this.ID_N, this.TYPE, this.DATE_N, this.TEXT_N);
		assertThat(track.equals(track2)).isFalse();
	}
}