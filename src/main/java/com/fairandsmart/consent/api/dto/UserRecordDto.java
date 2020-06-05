package com.fairandsmart.consent.api.dto;

public class UserRecordDto {
    private String bodyKey;
    private String owner;
    private String subject;
    private String value;

    public UserRecordDto() {
    }

    public String getBodyKey() {
        return bodyKey;
    }

    public void setBodyKey(String bodyKey) {
        this.bodyKey = bodyKey;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "UserRecordDto{" +
                "bodyKey='" + bodyKey + '\'' +
                ", owner='" + owner + '\'' +
                ", subject='" + subject + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
