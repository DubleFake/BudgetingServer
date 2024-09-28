package org.dfproductions.budgetingserver.web.security;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Primary
@Component
public class CustomUserDetailsService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User.UserBuilder builder = User.builder();
        builder.username(username);
        builder.password(passwordEncoder.encode(username));

        switch (username){

            case "user":
                builder.roles("USER");
                break;
            case "admin":
                builder.roles("USER","ADMIN");
                break;
            case "sadmin":
                builder.roles("USER","ADMIN","SUPERADMIN");
                break;
            default:
                throw new UsernameNotFoundException("User not found.");

        }

        return builder.build();

    }

}
