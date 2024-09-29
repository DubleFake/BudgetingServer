package org.dfproductions.budgetingserver.web.controllers;

import org.dfproductions.budgetingserver.backend.requests.PasswordRequest;
import org.dfproductions.budgetingserver.backend.services.UserService;
import org.dfproductions.budgetingserver.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GeneralController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public GeneralController(){}

    @ResponseStatus(HttpStatus.ACCEPTED)
    @GetMapping("/login")
    public ResponseEntity<String> validateUserPassword(@RequestBody PasswordRequest passwordRequest) {

        if(userService.validatePassword(passwordRequest.getEmail(),passwordRequest.getPassword())) {
           return new ResponseEntity<>("Granted.", HttpStatus.ACCEPTED);
        }else{
            return new ResponseEntity<>("Denied.", HttpStatus.FORBIDDEN);
        }

    }

    /*
    *
    * Record-related operations
    *
    * */

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/record")
    public void putNewRecord(@RequestBody String username, @RequestBody String info){}

    @GetMapping("/record")
    @ResponseBody
    public String getAllRecordsForUser(@RequestBody String username){
        return new String();
    }

    @DeleteMapping("/record/delete/{ID}")
    public void deleteRecord(@PathVariable String ID){}

}

