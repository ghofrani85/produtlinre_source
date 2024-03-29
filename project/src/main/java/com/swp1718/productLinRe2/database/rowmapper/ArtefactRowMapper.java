package com.swp1718.productLinRe2.database.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.swp1718.productLinRe2.database.helper.AssetType;
import com.swp1718.productLinRe2.model.Artefact;
import com.swp1718.productLinRe2.model.AudioArtefact;
import com.swp1718.productLinRe2.model.AudioAsset;
import com.swp1718.productLinRe2.model.Feature;
import com.swp1718.productLinRe2.model.OtherArtefact;
import com.swp1718.productLinRe2.model.OtherAsset;
import com.swp1718.productLinRe2.model.PictureArtefact;
import com.swp1718.productLinRe2.model.PictureAsset;
import com.swp1718.productLinRe2.model.TextArtefact;
import com.swp1718.productLinRe2.model.TextAsset;
import com.swp1718.productLinRe2.model.VideoArtefact;
import com.swp1718.productLinRe2.model.VideoAsset;
import com.swp1718.productLinRe2.model.XMLArtefact;
import com.swp1718.productLinRe2.model.XMLAsset;

/**
 * RowMapper for the artefact model.
 * 
 * @author Tobias Powelske
 * @author Jannik Gröger
 *
 */
public class ArtefactRowMapper implements RowMapper<Artefact> {

	@Override
	public Artefact mapRow(ResultSet rs, int rowNum) throws SQLException {
		AssetType type = AssetType.values()[rs.getInt("astype")];
		Artefact art = null;

		switch (type) {
		case AUDIO:
			art = new AudioArtefact(rs.getInt("artid"), rs.getString("title"),
					new AudioAsset(rs.getInt("assetid"), null, null, null), rs.getString("start"),
					rs.getString("endmark"));
			break;
		case PICTURE:
			art = new PictureArtefact(rs.getInt("artid"), rs.getString("title"),
					new PictureAsset(rs.getInt("assetid"), null, null, null), rs.getString("start"),
					rs.getString("endmark"), rs.getString("picturewidth"), rs.getString("pictureheight"));
			break;
		case TEXT:
			art = new TextArtefact(rs.getInt("artid"), rs.getString("title"),
					new TextAsset(rs.getInt("assetid"), null, null, null), rs.getString("start"),
					rs.getString("endmark"));
			break;
		case VIDEO:
			art = new VideoArtefact(rs.getInt("artid"), rs.getString("title"),
					new VideoAsset(rs.getInt("assetid"), null, null, null), rs.getString("start"),
					rs.getString("endmark"));
			break;
		case XML:
			art = new XMLArtefact(rs.getInt("artid"), rs.getString("title"),
					new XMLAsset(rs.getInt("assetid"), null, null, null), rs.getString("start"));
			break;
		case OTHER:
			art = new OtherArtefact(rs.getInt("artid"), rs.getString("title"),
					new OtherAsset(rs.getInt("assetid"), null, null, null), rs.getString("start"),
					rs.getString("endmark"));
			break;
		default:
			return null;
		}

		art.setFeature(new Feature(rs.getInt("featureid"), null, null, null));

		return art;
	}

}
