package org.dfproductions.budgetingserver.web.controllers;

import org.dfproductions.budgetingserver.backend.services.RecordService;
import org.dfproductions.budgetingserver.backend.templates.Record;
import org.dfproductions.budgetingserver.backend.templates.requests.RecordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/record")
public class RecordController {

    @Autowired
    RecordService recordService;

    @PostMapping("/create")
    public ResponseEntity<String> createNewRecord(@RequestBody RecordRequest recordRequest){

        Record savedRecord;
        try {
            savedRecord = recordService.createRecord(
                    recordRequest.getRecord(),
                    recordRequest.getEmail()
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
    public ResponseEntity<List<RecordRequest>> getAllRecordsForPeriod(@RequestBody String email, @PathVariable String date) {

        List<RecordRequest> recordRequests = new ArrayList<>();
        List<Record> records = recordService.getRecordsForPeriod(date);

        for(Record record : records){
            record.setUserId(-1);
            recordRequests.add(new RecordRequest(record, email));
        }

        return new ResponseEntity<>(recordRequests, HttpStatus.OK);

    }

    @DeleteMapping("/record/delete/{ID}")
    public ResponseEntity<String> deleteRecord(@PathVariable int id){
        if(recordService.deleteRecord(id))
            return new ResponseEntity<>("Deleted.", HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

}
