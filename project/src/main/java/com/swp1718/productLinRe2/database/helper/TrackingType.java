package com.swp1718.productLinRe2.database.helper;

/**
 * Enum for different types of tracking.
 * 
 * @author Tobias Powelske
 *
 */
public enum TrackingType {
	PROJECT(0), PRODUCT(1), FEATURE(2), ASSET(3), ARTEFACT(4), USER(5);

	private final int value;

	/**
	 * Constructor
	 * 
	 * @param value
	 *            value of the enum
	 */
	private TrackingType(int value) {
		this.value = value;
	}

	/**
	 * Gets the value of the enum.
	 * 
	 * @return the value of the enum
	 */
	public int getValue() {
		return value;
	}
}
