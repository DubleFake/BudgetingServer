package org.dfproductions.budgetingserver.web.controllers;

import org.dfproductions.budgetingserver.backend.PasswordHandler;
import org.dfproductions.budgetingserver.backend.repositories.PasswordRepository;
import org.dfproductions.budgetingserver.backend.repositories.UserRepository;
import org.dfproductions.budgetingserver.backend.services.TokenBlacklistService;
import org.dfproductions.budgetingserver.backend.templates.Password;
import org.dfproductions.budgetingserver.backend.templates.requests.PasswordRequest;
import org.dfproductions.budgetingserver.backend.templates.requests.UserRequest;
import org.dfproductions.budgetingserver.backend.services.UserService;
import org.dfproductions.budgetingserver.backend.templates.User;
import org.dfproductions.budgetingserver.web.security.interfaces.TokenStore;
import org.dfproductions.budgetingserver.web.security.jwt.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private JwtUtility jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    private final UserRepository userRepository;
    private final PasswordRepository passwordRepository;
    private final TokenStore tokenStore; // Inject the TokenStore service

    public UserController(UserRepository userRepository, PasswordRepository passwordRepository, TokenStore tokenStore) {
        this.userRepository = userRepository;
        this.passwordRepository = passwordRepository;
        this.tokenStore = tokenStore;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {

        User savedUser;
        try {
            savedUser = userService.createUser(
                    userRequest.getName(),
                    userRequest.getEmail(),
                    userRequest.getPasswordHash(),
                    userRequest.getPasswordSalt()

            );
            if(savedUser == null)
                return new ResponseEntity<>("Bad data.", HttpStatus.BAD_REQUEST);

            return new ResponseEntity<>("User created.", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            if(e.getMessage().contains("unique_email")) {
                return new ResponseEntity<>("Email already taken.", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Denied.", HttpStatus.FORBIDDEN);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody PasswordRequest passwordRequest) throws NoSuchAlgorithmException {
        // Fetch user from the database
        User user = userRepository.findByEmail(passwordRequest.getEmail());
        if (user == null) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }

        // Retrieve the password using passwordId
        Password password = passwordRepository.findPasswordById(user.getPasswordId());

        // Use your PasswordHandler to verify the provided password against the stored hash
        boolean isPasswordValid = PasswordHandler.verifyPassword(passwordRequest.getPassword(), password.getPasswordSalt() + ":" + password.getPasswordHash());
        if (!isPasswordValid) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }

        // Check if a token already exists for the user
        Optional<String> existingToken = tokenStore.getToken(passwordRequest.getEmail());

        if (existingToken.isPresent() && !jwtUtil.isTokenExpired(existingToken.get()) && !tokenBlacklistService.isTokenBlacklisted(existingToken.get())) {
            // If a valid token is found, return a message
            tokenBlacklistService.blacklistToken(existingToken.get());
        }

        String token = jwtUtil.generateToken(passwordRequest.getEmail());
        tokenStore.saveToken(passwordRequest.getEmail(), token, /*expiry time*/ 3600); // e.g., 1 hour expiry


        // If password is valid, generate JWT token
        return new ResponseEntity<>(jwtUtil.generateToken(passwordRequest.getEmail()), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            tokenBlacklistService.blacklistToken(jwt); // Blacklist the JWT
        }

        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }


    @DeleteMapping("/delete/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email, Authentication authentication){

        if(userService.deleteUser(email, authentication))
            return new ResponseEntity<>("Deleted.", HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/recovery/{email}")
    public ResponseEntity<String> recoveryRequest(@PathVariable String email) {
        try {
            userService.attemptRecovery(email);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Denied.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }

    }

