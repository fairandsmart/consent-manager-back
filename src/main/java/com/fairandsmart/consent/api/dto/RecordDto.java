package com.fairandsmart.consent.api.dto;

import com.fairandsmart.consent.manager.entity.Record;

public class RecordDto {

    private String serial;
    private String headerKey;
    private String bodyKey;
    private String footerKey;
    private String subject;
    private long creationTimestamp;
    private long expirationTimestamp;
    private String type;
    private String value;
    private String status;
    private String collectionMethod;
    private String comment;

    public RecordDto() {
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getHeaderKey() {
        return headerKey;
    }

    public void setHeaderKey(String headerKey) {
        this.headerKey = headerKey;
    }

    public String getBodyKey() {
        return bodyKey;
    }

    public void setBodyKey(String bodyKey) {
        this.bodyKey = bodyKey;
    }

    public String getFooterKey() {
        return footerKey;
    }

    public void setFooterKey(String footerKey) {
        this.footerKey = footerKey;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public long getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public void setExpirationTimestamp(long expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCollectionMethod() {
        return collectionMethod;
    }

    public void setCollectionMethod(String collectionMethod) {
        this.collectionMethod = collectionMethod;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static final RecordDto fromRecord(Record record) {
        RecordDto dto = new RecordDto();
        dto.setSubject(record.subject);
        dto.setSerial(record.serial);
        dto.setHeaderKey(record.headKey);
        dto.setBodyKey(record.bodyKey);
        dto.setFooterKey(record.footKey);
        dto.setCreationTimestamp(record.creationTimestamp);
        dto.setExpirationTimestamp(record.expirationTimestamp);
        dto.setCollectionMethod(record.collectionMethod.toString());
        dto.setType(record.type);
        dto.setComment(record.comment);
        dto.setStatus(record.status.toString());
        dto.setValue(record.value);
        return dto;
    }
}
