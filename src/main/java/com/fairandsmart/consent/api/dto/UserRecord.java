package com.fairandsmart.consent.api.dto;

public class UserRecord {
    private String bodyKey;
    private String owner;
    private String subject;
    private long creationTimestamp;
    private long expirationTimestamp;
    private String type;
    private String value;
    private String status;

    public UserRecord() {
    }

    public String getBodyKey() {
        return bodyKey;
    }

    public UserRecord setBodyKey(String bodyKey) {
        this.bodyKey = bodyKey;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public UserRecord setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public UserRecord setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public UserRecord setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
        return this;
    }

    public long getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public UserRecord setExpirationTimestamp(long expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
        return this;
    }

    public String getType() {
        return type;
    }

    public UserRecord setType(String type) {
        this.type = type;
        return this;
    }

    public String getValue() {
        return value;
    }

    public UserRecord setValue(String value) {
        this.value = value;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public UserRecord setStatus(String status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "UserRecord{" +
                "bodyKey='" + bodyKey + '\'' +
                ", owner='" + owner + '\'' +
                ", subject='" + subject + '\'' +
                ", creationTimestamp=" + creationTimestamp +
                ", expirationTimestamp=" + expirationTimestamp +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
