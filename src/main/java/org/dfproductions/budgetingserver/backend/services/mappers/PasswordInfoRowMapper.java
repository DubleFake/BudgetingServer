package org.dfproductions.budgetingserver.backend.services.mappers;
import org.dfproductions.budgetingserver.backend.templates.PasswordInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PasswordInfoRowMapper implements RowMapper<PasswordInfo> {

    @Override
    public PasswordInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        PasswordInfo passwordInfo = new PasswordInfo();
        passwordInfo.setPasswordHash(rs.getString("PasswordHash"));
        passwordInfo.setPasswordSalt(rs.getString("PasswordSalt"));
        return passwordInfo;
    }
}
