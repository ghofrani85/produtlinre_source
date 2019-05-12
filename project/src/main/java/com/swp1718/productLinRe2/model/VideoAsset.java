package com.swp1718.productLinRe2.model;

import com.swp1718.productLinRe2.database.helper.AssetType;

/**
 * When an instance of this class is created it creates a video type asset.
 * 
 * @author Stefan Schmidt
 *
 */
public class VideoAsset extends Asset {

	/**
	 * Default Constructor
	 */
	public VideoAsset() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            id of the asset
	 * @param title
	 *            title of the asset
	 * @param description
	 *            description of the asset
	 * @param URL
	 *            an URL-reference to the file this asset is representing
	 */
	public VideoAsset(Integer id, String title, String description, String URL) {
		super(id, title, description, URL, AssetType.VIDEO);
	}

}
