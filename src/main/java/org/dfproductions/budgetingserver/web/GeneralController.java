package org.dfproductions.budgetingserver.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GeneralController {

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
    public void putNewUser() {}

    @GetMapping("/user")
    @ResponseBody
    public String checkIfUserExists(){
        return new String();
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
