package com.swp1718.productLinRe2.database.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.swp1718.productLinRe2.controller.token.PasswordResetToken;
import com.swp1718.productLinRe2.model.User;

/**
 * RowMapper for the password reset tokens.
 * 
 * @author Tobias Powelske
 *
 */
public class PasswordResetTokenRowMapper implements RowMapper<PasswordResetToken> {

	@Override
	public PasswordResetToken mapRow(ResultSet rs, int rowNum) throws SQLException {
		PasswordResetToken pwtoken = new PasswordResetToken();

		pwtoken.setId(rs.getInt("id"));
		pwtoken.setToken(rs.getString("token"));
		pwtoken.setExpirationDate(rs.getDate("expirationdate"));
		pwtoken.setUser(new User(rs.getInt("userid"), null, null, null, false, false, null, null, null, null));

		return pwtoken;
	}

}
