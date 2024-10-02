package org.dfproductions.budgetingserver.backend.services;

import org.dfproductions.budgetingserver.backend.services.mappers.RecordRowMapper;
import org.dfproductions.budgetingserver.backend.services.mappers.UserRowMapper;
import org.dfproductions.budgetingserver.backend.templates.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.dfproductions.budgetingserver.backend.templates.Record;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Service
public class RecordService {

    @Autowired
    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Transactional
    public Record createRecord(Record record, String email) {

        Integer userId = jdbcTemplate.queryForObject("SELECT ID FROM users WHERE Email = ?", Integer.class, email);
        int id = (userId != null) ? userId : -1;
        if(id == -1)
            return null;

        KeyHolder recordKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO records (Category, Date, Price, Place, Note, UserID) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, record.getCategory());
            ps.setString(2, record.getDate());
            ps.setDouble(3, record.getPrice());
            ps.setString(4, record.getPlace());
            ps.setString(5, record.getNote());
            ps.setInt(6, id);
            return ps;
        }, recordKeyHolder);

        int recordId = recordKeyHolder.getKey().intValue();

        return jdbcTemplate.queryForObject("SELECT * FROM records WHERE ID = ?", new RecordRowMapper(), recordId);

    }

    @Transactional
    public List<Record> getRecordsForPeriod(String date) {
        return jdbcTemplate.query("SELECT r.ID, Category, Date, Price, Place, Note, UserID FROM records as r LEFT JOIN users AS u ON r.UserID = u.ID WHERE Date LIKE '" + date +"%'", new RecordRowMapper());
    }


    @Transactional
    public boolean deleteRecord(int id){

        try{
            int rowsAffected = jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "DELETE FROM records WHERE ID=?");
                ps.setInt(1, id);
                return ps;
            });


            return rowsAffected > 0;

        }catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

}
