package org.dfproductions.budgetingserver.backend.templates.requests;

import org.dfproductions.budgetingserver.backend.templates.Record;

public class RecordRequest {

    private Record record;
    private String email;

    public RecordRequest(Record record, String email) {
        this.record = record;
        this.email = email;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
