package org.dfproductions.budgetingserver.backend.services.mappers;

import org.dfproductions.budgetingserver.backend.templates.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("ID"));
        user.setEmail(rs.getString("Email"));
        user.setName(rs.getString("Name"));
        user.setPasswordId(rs.getInt("PasswordID"));
        user.setRole(rs.getString("Role"));
        return user;
    }
}
