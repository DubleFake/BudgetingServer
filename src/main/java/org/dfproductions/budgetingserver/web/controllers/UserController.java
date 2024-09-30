package org.dfproductions.budgetingserver.web.controllers;

import org.dfproductions.budgetingserver.backend.requests.PasswordRequest;
import org.dfproductions.budgetingserver.backend.requests.UserRequest;
import org.dfproductions.budgetingserver.backend.services.UserService;
import org.dfproductions.budgetingserver.backend.templates.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {

        try {
            User savedUser = userService.createUser(
                    userRequest.getName(),
                    userRequest.getEmail(),
                    userRequest.getPasswordHash(),
                    userRequest.getPasswordSalt()
            );
            return new ResponseEntity<>("User created.", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            if(e.getMessage().contains("unique_email")) {
                return new ResponseEntity<>("Email already taken.", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Denied.", HttpStatus.FORBIDDEN);
        }

    }

    @GetMapping("/login")
    public ResponseEntity<String> validateUserPassword(@RequestBody PasswordRequest passwordRequest) {

        if(userService.validatePassword(passwordRequest.getEmail(),passwordRequest.getPassword())) {
            return new ResponseEntity<>("Granted.", HttpStatus.ACCEPTED);
        }else{
            return new ResponseEntity<>("Denied.", HttpStatus.FORBIDDEN);
        }

    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email){

        if(userService.deleteUser(email))
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    }

