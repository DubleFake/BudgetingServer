package org.dfproductions.budgetingserver.web.security;

import org.dfproductions.budgetingserver.backend.repositories.PasswordRepository;
import org.dfproductions.budgetingserver.backend.repositories.UserRepository;
import org.dfproductions.budgetingserver.backend.templates.Password;
import org.dfproductions.budgetingserver.backend.templates.User;
import org.dfproductions.budgetingserver.web.security.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Primary
@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordRepository passwordRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, PasswordRepository passwordRepository) {
        this.userRepository = userRepository;
        this.passwordRepository = passwordRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Fetch user from the database
        User user = userRepository.findByEmail(email);

        // Retrieve the password using passwordId
        Password password = passwordRepository.findPasswordById(user.getPasswordId());

        // Create a UserDetails object without exposing the password directly
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),        // Using email as the username
                password.getPasswordHash(), // Return password hash for internal use
                true,                  // account is enabled
                true,                  // account is non-expired
                true,                  // credentials are non-expired
                true,                  // account is non-locked
                user.getAuthorities()   // Fetch user's roles/authorities
        );
    }

}
