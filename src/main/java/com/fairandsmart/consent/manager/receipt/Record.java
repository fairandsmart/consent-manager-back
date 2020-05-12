package com.fairandsmart.consent.manager.receipt;

import java.util.List;

public class Record {

    private String serial;
    private String body;
    private String value;
    private List<Sharing> sharings;

    public Record() {
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Sharing> getSharings() {
        return sharings;
    }

    public void setSharings(List<Sharing> sharings) {
        this.sharings = sharings;
    }
}
