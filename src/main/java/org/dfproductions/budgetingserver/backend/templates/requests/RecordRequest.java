package org.dfproductions.budgetingserver.backend.templates.requests;

import org.dfproductions.budgetingserver.backend.templates.Record;

public class RecordRequest {

    private Record record;

    public RecordRequest(Record record) {
        this.record = record;
    }

    public RecordRequest() {}

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }
}
