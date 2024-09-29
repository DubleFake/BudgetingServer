package org.dfproductions.budgetingserver.backend.services;

import org.dfproductions.budgetingserver.backend.repositories.PasswordRepository;
import org.dfproductions.budgetingserver.backend.repositories.UserRepository;
import org.dfproductions.budgetingserver.backend.templates.Password;
import org.dfproductions.budgetingserver.backend.templates.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class UserRowMapper implements RowMapper<User> {

    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("ID")); // assuming the primary key column is 'ID'
        user.setEmail(rs.getString("Email"));
        user.setName(rs.getString("Name"));
        user.setPasswordId(rs.getInt("PasswordID"));
        return user;
    }
}

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    JdbcTemplate jdbcTemplate = new JdbcTemplate();

   /* @Transactional
    public User createUser(String name, String email, String passwordHash, String passwordSalt) {
        // Step 1: Create and save the User
        User user = new User();
        user.setName(name);
        user.setEmail(email);

        // Step 2: Create Password
        Password password = new Password();
        password.setPasswordHash(passwordHash);
        password.setPasswordSalt(passwordSalt);

        // Save User
        User savedUser = userRepository.save(user);

        // Save the Password first to get its ID
        Password savedPassword = passwordRepository.save(password);
        user.setPasswordId(savedPassword.getId()); // Set the Password ID to User



        return savedUser;
    }*/
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

        Long userId = userKeyHolder.getKey().longValue();  // Get the generated UserID

        // Step 2: Insert Password with the UserID reference
        KeyHolder passwordKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO passwords (PasswordHash, PasswordSalt, UserID) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, passwordHash);
            ps.setString(2, passwordSalt);
            ps.setLong(3, userId);  // Use the UserID from the previous step
            return ps;
        }, passwordKeyHolder);

        Long passwordId = passwordKeyHolder.getKey().longValue();  // Get the generated PasswordID

        // Step 3: Update User with the PasswordID
        jdbcTemplate.update("UPDATE users SET PasswordID = ? WHERE ID = ?", passwordId, userId);

        // Return the newly created user (optional)
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE ID = ?", new Object[]{userId}, new UserRowMapper());
    }

}
