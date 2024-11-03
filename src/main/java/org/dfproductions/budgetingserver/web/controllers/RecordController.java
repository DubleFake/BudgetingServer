package org.dfproductions.budgetingserver.web.controllers;

import org.dfproductions.budgetingserver.backend.services.RecordService;
import org.dfproductions.budgetingserver.backend.templates.Record;
import org.dfproductions.budgetingserver.backend.templates.requests.RecordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/record")
public class RecordController {

    @Autowired
    RecordService recordService;

    @PostMapping("/create")
    public ResponseEntity<String> createNewRecord(@RequestBody RecordRequest recordRequest){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email;

        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            email = userDetails.getUsername();
        } else {
            email = authentication.getPrincipal().toString();
        }

        Record savedRecord;
        try {
            savedRecord = recordService.createRecord(
                    recordRequest.getRecord(),
                    email
            );
            if(savedRecord == null)
                return new ResponseEntity<>("Bad data.", HttpStatus.BAD_REQUEST);

            return new ResponseEntity<>("Record created.", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Denied.", HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/get/{date}")
    @ResponseBody
    public ResponseEntity<List<RecordRequest>> getAllRecordsForPeriod(@PathVariable String date) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email;

        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            email = userDetails.getUsername();
        } else {
            email = authentication.getPrincipal().toString();
        }

        List<Record> records;

        if(date.contains("-")) {
            String[] dates = date.split("-");
            records = recordService.getRecordsForDateRange(dates[0], dates[1], email);
        }
        else{
            records = recordService.getRecordsForDate(date, email);
        }

        List<RecordRequest> recordRequests = new ArrayList<>();


        for(Record record : records){
            record.setUserId(-1);
            recordRequests.add(new RecordRequest(record));
        }

        return new ResponseEntity<>(recordRequests, HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRecord(@PathVariable int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email;

        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            email = userDetails.getUsername();
        } else {
            email = authentication.getPrincipal().toString();
        }
        if(recordService.deleteRecord(id, email))
            return new ResponseEntity<>("Deleted.", HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateRecord(@RequestBody Record record){
        Record updatedRecord;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email;

        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            email = userDetails.getUsername();
        } else {
            email = authentication.getPrincipal().toString();
        }

        try {
            updatedRecord = recordService.updateRecord(record, email);

            if(updatedRecord == null)
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

            return new ResponseEntity<>("Record updated.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Denied.", HttpStatus.FORBIDDEN);
        }
    }

}
