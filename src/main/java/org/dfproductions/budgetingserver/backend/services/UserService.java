package org.dfproductions.budgetingserver.backend.services;

import org.dfproductions.budgetingserver.backend.EmailService;
import org.dfproductions.budgetingserver.backend.PasswordHandler;
import org.dfproductions.budgetingserver.backend.services.mappers.PasswordInfoRowMapper;
import org.dfproductions.budgetingserver.backend.services.mappers.UserRowMapper;
import org.dfproductions.budgetingserver.backend.templates.PasswordInfo;
import org.dfproductions.budgetingserver.backend.templates.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    @Autowired
    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Autowired
    private EmailService emailService;

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
    public String getPasswordHash(String email, String password) {

        try {

            List<PasswordInfo> pi =  jdbcTemplate.query("SELECT PasswordHash, PasswordSalt FROM passwords AS p LEFT JOIN users AS u ON p.UserID = u.ID WHERE email = ?;", new PasswordInfoRowMapper(), email);
            if(pi.isEmpty())
                return "";
            String passwordHash = pi.get(0).getPasswordSalt() + ":" + pi.get(0).getPasswordHash();
            if(PasswordHandler.verifyPassword(password, passwordHash)) {
                return pi.get(0).getPasswordHash();
            }


        }catch (NoSuchAlgorithmException ex){
            ex.printStackTrace();
        }

        return "";

    }

    @Transactional
    public boolean deleteUser(String email, Authentication authentication) {
        int rowsAffected = 0;
        try{
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
                rowsAffected = jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "DELETE FROM users WHERE email=?");
                    ps.setString(1, email);
                    return ps;
                });


                return rowsAffected > 0;
            }else {
                return false;
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    @Transactional
    public void attemptRecovery(String email) throws NoSuchAlgorithmException {

        Integer userId = jdbcTemplate.queryForObject("SELECT ID FROM users WHERE email = ?", Integer.class, email);

        if(userId != null && userId > 0) {
            String tempPass = PasswordHandler.generatePassword(20);
            String[] hashedPass = PasswordHandler.hashPassword(tempPass).split(":");
            createPassword(userId,hashedPass[1],hashedPass[0]);
            emailService.sendRecoveryEmail(email, tempPass);
        }
    }

}
