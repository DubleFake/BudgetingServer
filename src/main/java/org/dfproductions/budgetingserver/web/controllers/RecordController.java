package org.dfproductions.budgetingserver.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/record")
public class RecordController {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public void createNewRecord(@RequestBody String username, @RequestBody String info){

    }

    @GetMapping("/record")
    @ResponseBody
    public String getAllRecordsForUser(@RequestBody String username){
        return new String();
    }

    @DeleteMapping("/record/delete/{ID}")
    public void deleteRecord(@PathVariable String ID){}

}
