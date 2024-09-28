package org.dfproductions.budgetingserver.web.controllers;

import org.dfproductions.budgetingserver.backend.templates.User;
import org.dfproductions.budgetingserver.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GeneralController {

    @Autowired
    private UserRepository userRepository;

    public GeneralController(){}

    /*
    *
    * User-related operations
    *
    * */

    @DeleteMapping("/user/delete/{username}")
    public void deleteUser(@PathVariable String username){}

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user")
    public ResponseEntity<User> putNewUser(@RequestBody String name,
                                           @RequestBody String email,
                                           @RequestBody String password) {
        return null;

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

