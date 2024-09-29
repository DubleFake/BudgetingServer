package org.dfproductions.budgetingserver.backend.services;

import org.dfproductions.budgetingserver.backend.PasswordHashing;
import org.dfproductions.budgetingserver.backend.templates.Password;
import org.dfproductions.budgetingserver.backend.templates.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.RowMapper;

import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class UserRowMapper implements RowMapper<User> {

    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("ID"));
        user.setEmail(rs.getString("Email"));
        user.setName(rs.getString("Name"));
        user.setPasswordId(rs.getInt("PasswordID"));
        return user;
    }
}

class PasswordInfo {
    private String passwordHash;
    private String passwordSalt;

    // Getters and Setters
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }
}

class PasswordInfoRowMapper implements RowMapper<PasswordInfo> {

    @Override
    public PasswordInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        PasswordInfo passwordInfo = new PasswordInfo();
        passwordInfo.setPasswordHash(rs.getString("PasswordHash"));
        passwordInfo.setPasswordSalt(rs.getString("PasswordSalt"));
        return passwordInfo;
    }
}



@Service
public class UserService {

    @Autowired
    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Transactional
    public User createUser(String name, String email, String passwordHash, String passwordSalt) {

        // Step 1: Insert User (without PasswordID)
        KeyHolder userKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO users (Email, Name) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, email);
            ps.setString(2, name);
            return ps;
        }, userKeyHolder);

        int userId = userKeyHolder.getKey().intValue();   // Get the generated UserID

        // Step 2: Insert Password with the UserID reference

        boolean creationResult = createPassword(userId, passwordHash, passwordSalt);

        if(creationResult) {
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE ID = ?", new UserRowMapper(), userId);
        }else{
            return new User();
        }
    }

    @Transactional
    private boolean createPassword(int userId, String passwordHash, String passwordSalt) {

        try {
            KeyHolder passwordKeyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO passwords (PasswordHash, PasswordSalt, UserID) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, passwordHash);
                ps.setString(2, passwordSalt);
                ps.setInt(3, userId);  // Use the UserID from the previous step
                return ps;
            }, passwordKeyHolder);

            int passwordId = passwordKeyHolder.getKey().intValue();  // Get the generated PasswordID

            // Step 3: Update User with the PasswordID
            jdbcTemplate.update("UPDATE users SET PasswordID = ? WHERE ID = ?", passwordId, userId);

            return true;
        }catch (Exception e){
            return false;
        }


    }

    @Transactional
    public boolean validatePassword(String email, String password) {

        try {
            PasswordInfo pi =  jdbcTemplate.queryForObject("SELECT PasswordHash, PasswordSalt FROM passwords AS p LEFT JOIN users AS u ON p.UserID = u.ID WHERE email = ?;", new PasswordInfoRowMapper(), email);
            String passwordHash = pi.getPasswordSalt() + ":" + pi.getPasswordHash();
            return PasswordHashing.verifyPassword(password, passwordHash);


        }catch (NoSuchAlgorithmException ex){
            ex.printStackTrace();
            return false;
        }

    }

}
