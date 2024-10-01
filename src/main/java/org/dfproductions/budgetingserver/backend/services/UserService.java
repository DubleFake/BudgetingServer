package org.dfproductions.budgetingserver.backend.services;

import org.dfproductions.budgetingserver.backend.PasswordHashing;
import org.dfproductions.budgetingserver.backend.services.mappers.PasswordInfoRowMapper;
import org.dfproductions.budgetingserver.backend.services.mappers.UserRowMapper;
import org.dfproductions.budgetingserver.backend.templates.PasswordInfo;
import org.dfproductions.budgetingserver.backend.templates.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Service
public class UserService {

    @Autowired
    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Transactional
    public User createUser(String name, String email, String passwordHash, String passwordSalt) {

        if(name.isEmpty() || email.isEmpty() || passwordHash.isEmpty() || passwordSalt.isEmpty())
            return null;

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
            return null;
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

            List<PasswordInfo> pi =  jdbcTemplate.query("SELECT PasswordHash, PasswordSalt FROM passwords AS p LEFT JOIN users AS u ON p.UserID = u.ID WHERE email = ?;", new PasswordInfoRowMapper(), email);
            if(pi.isEmpty())
                return false;
            String passwordHash = pi.get(0).getPasswordSalt() + ":" + pi.get(0).getPasswordHash();
            return PasswordHashing.verifyPassword(password, passwordHash);


        }catch (NoSuchAlgorithmException ex){
            ex.printStackTrace();
            return false;
        }

    }

    @Transactional
    public boolean deleteUser(String email) {

        try{
            int rowsAffected = jdbcTemplate.update(connection -> {
               PreparedStatement ps = connection.prepareStatement(
                 "DELETE FROM users WHERE email=?");
               ps.setString(1, email);
               return ps;
            });


            return rowsAffected > 0;

        }catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

}
