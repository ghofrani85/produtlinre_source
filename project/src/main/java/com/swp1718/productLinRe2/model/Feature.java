package com.swp1718.productLinRe2.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class provides all information for a feature.
 * 
 * A feature is an object that has one or more artefacts.
 * 
 * @author Stefan Schmidt
 *
 */
public class Feature {

	private Integer id;
	private String title;
	private String description;
	private List<Artefact> artefactList;
	private Feature parent;
	private boolean updatedparent;
	private List<Tracking> trackingList;

	/**
	 * Default Constructor.
	 */
	public Feature() {
		this.id = null;
		this.title = "";
		this.description = "";
		this.artefactList = new ArrayList<>();
		this.trackingList = new LinkedList<>();
		this.updatedparent = false;
	}

	/**
	 * Constructor for a new Feature.
	 * 
	 * @param title
	 *            The title for the feature
	 * @param description
	 *            The description of the feature
	 */
	public Feature(String title, String description) {
		this.id = null;
		this.title = title;
		this.description = description;
		this.artefactList = new ArrayList<>();
		this.parent = null;
		this.trackingList = new LinkedList<>();
		this.updatedparent = false;
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            id of the feature
	 * @param title
	 *            title of the feature
	 * @param description
	 *            description of the feature
	 * @param artefactList
	 *            List of artefacts for feature
	 */
	public Feature(Integer id, String title, String description, List<Artefact> artefactList) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.artefactList = artefactList;
		this.parent = null;
		this.trackingList = new LinkedList<>();
		this.updatedparent = false;
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            id of the feature
	 * @param title
	 *            title of the feature
	 * @param description
	 *            description of the feature
	 * @param artefactList
	 *            List of artefacts for feature
	 * @param parent
	 *            the Feature this was copied from
	 */
	public Feature(Integer id, String title, String description, List<Artefact> artefactList, Feature parent) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.artefactList = artefactList;
		this.parent = parent;
		this.trackingList = new LinkedList<>();
		this.updatedparent = false;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the assetList
	 */
	public List<Artefact> getArtefactList() {
		return artefactList;
	}

	/**
	 * @param artefactList
	 *            the assetList to set
	 */
	public void setArtefactList(List<Artefact> artefactList) {
		this.artefactList = artefactList;
	}

	/**
	 * @return the parent
	 */
	public Feature getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(Feature parent) {
		this.parent = parent;
	}

	/**
	 * @return the trackinglist
	 */
	public List<Tracking> getTrackingList() {
		return trackingList;
	}

	/**
	 * @param trackinglist
	 *            the trackinglist to set
	 */
	public void setTrackingList(List<Tracking> trackinglist) {
		this.trackingList = trackinglist;
	}

	/**
	 * @return the updatedparent
	 */
	public boolean isUpdatedparent() {
		return updatedparent;
	}

	/**
	 * @param updatedparent
	 *            the updatedparent to set
	 */
	public void setUpdatedparent(boolean updatedparent) {
		this.updatedparent = updatedparent;
	}

	public void addArtefact(Artefact artefact) {
		if (artefactList == null) {
			artefactList = new ArrayList<>();
		}
		artefactList.add(artefact);
	}

	public void removeArtefact(Artefact artefact) {
		artefactList.remove(artefact);
	}

	/**
	 * Adds a tracking object to the tracking list.
	 * 
	 * @param tracking
	 *            the object to be added
	 */
	public void addTrackingEntry(Tracking tracking) {
		if (trackingList == null) {
			trackingList = new LinkedList<>();
		}
		trackingList.add(tracking);
	}

	/**
	 * Removes a tracking object from the tracking list.
	 * 
	 * @param tracking
	 *            the object to be removed
	 */
	public void removeTrackingEntry(Tracking tracking) {
		trackingList.remove(tracking);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artefactList == null) ? 0 : artefactList.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((trackingList == null) ? 0 : trackingList.hashCode());
		result = prime * result + (updatedparent ? 1231 : 1237);
		return result;
	}

	/**
	 * Equals method, only compares the ids because the content is saved in the
	 * database.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Feature)) {
			return false;
		}
		Feature other = (Feature) obj;
		if (this.id == null || other.getId() == null) {
			return false;
		}
		return this.id.equals(other.id);
	}
}
