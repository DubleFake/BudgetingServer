package org.dfproductions.budgetingserver.backend.services.mappers;

import org.dfproductions.budgetingserver.backend.templates.Record;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RecordRowMapper implements RowMapper<Record> {

    public Record mapRow(ResultSet rs, int rowNum) throws SQLException {
        Record record = new Record();
        record.setId(rs.getInt("ID"));
        record.setCategory(rs.getString("Category"));
        record.setDate(rs.getString("Date"));
        record.setNote(rs.getString("Note"));
        record.setPlace(rs.getString("Place"));
        record.setPrice(rs.getDouble("Price"));
        record.setUserId(rs.getInt("UserID"));
        record.setType(rs.getString("Type"));
        return record;
    }
}
