package org.dfproductions.budgetingserver.web.controllers;

import org.dfproductions.budgetingserver.backend.UserRequest;
import org.dfproductions.budgetingserver.backend.services.UserService;
import org.dfproductions.budgetingserver.backend.templates.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody UserRequest userRequest) {

        try {
            User savedUser = userService.createUser(
                    userRequest.getName(),
                    userRequest.getEmail(),
                    userRequest.getPasswordHash(),
                    userRequest.getPasswordSalt()
            );
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
