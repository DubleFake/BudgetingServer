package org.dfproductions.budgetingserver.backend.services;

import org.dfproductions.budgetingserver.backend.repositories.PasswordRepository;
import org.dfproductions.budgetingserver.backend.repositories.UserRepository;
import org.dfproductions.budgetingserver.backend.templates.Password;
import org.dfproductions.budgetingserver.backend.templates.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRepository passwordRepository;

    @Transactional
    public User createUser(String name, String email, String passwordHash, String passwordSalt) {
        // Step 1: Create and save the User
        User user = new User();
        user.setName(name);
        user.setEmail(email);

        // Step 2: Create Password
        Password password = new Password();
        password.setHash(passwordHash);
        password.setSalt(passwordSalt);
        password.setUser(user); // set the relationship

        // Set password in user and save user
        user.setPassword(password);
        User savedUser = userRepository.save(user);

        return savedUser;
    }
}
