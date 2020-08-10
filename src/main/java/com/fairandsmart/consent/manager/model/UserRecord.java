package com.fairandsmart.consent.manager.model;

import com.fairandsmart.consent.manager.entity.ModelEntry;
import com.fairandsmart.consent.manager.entity.Record;
import com.fairandsmart.consent.manager.filter.SortableFilter;
import org.apache.commons.lang3.StringUtils;

public class UserRecord {
    private String headerKey;
    private String bodyKey;
    private String footerKey;
    private String owner;
    private String subject;
    private long creationTimestamp;
    private long expirationTimestamp;
    private String type;
    private String value;
    private String status;
    private String collectionMethod;
    private String comment;

    public UserRecord() {
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

    @Override
    public String toString() {
        return "UserRecord{" +
                "headerKey='" + headerKey + '\'' +
                ", bodyKey='" + bodyKey + '\'' +
                ", footerKey='" + footerKey + '\'' +
                ", owner='" + owner + '\'' +
                ", subject='" + subject + '\'' +
                ", creationTimestamp=" + creationTimestamp +
                ", expirationTimestamp=" + expirationTimestamp +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", status='" + status + '\'' +
                ", collectionMethod='" + collectionMethod + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }

    public int compare(UserRecord other, SortableFilter filter) {
        int result = 0;
        switch(filter.getOrder()) {
            case "creationTimestamp":
                result = Long.compare(creationTimestamp, other.creationTimestamp);
                break;
            case "expirationTimestamp":
                result = Long.compare(expirationTimestamp, other.expirationTimestamp);
                break;
            case "type":
                result = StringUtils.compare(type, other.type);
                break;
            case "value":
                result = StringUtils.compare(value, other.value);
                break;
            case "comment":
                result = StringUtils.compare(comment, other.comment);
                break;
            case "collectionMethod":
                result = StringUtils.compare(collectionMethod, other.collectionMethod);
                break;
            case "bodyKey":
            default:
                result = StringUtils.compare(bodyKey, other.bodyKey);
                break;
        }
        return "desc".equals(filter.getDirection()) ? -result : result;
    }

    public static UserRecord fromRecord(Record record) {
        UserRecord userRecord = new UserRecord();
        userRecord.setHeaderKey(record.headKey);
        userRecord.setBodyKey(record.bodyKey);
        userRecord.setFooterKey(record.footKey);
        userRecord.setOwner(record.owner);
        userRecord.setSubject(record.subject);
        userRecord.setCreationTimestamp(record.creationTimestamp);
        userRecord.setExpirationTimestamp(record.expirationTimestamp);
        userRecord.setType(record.type);
        userRecord.setValue(record.value);
        userRecord.setStatus(record.status.toString());
        userRecord.setCollectionMethod(record.collectionMethod.toString());
        userRecord.setComment(record.comment);
        return userRecord;
    }

    public static UserRecord fromEntryAndSubject(ModelEntry entry, String subject) {
        UserRecord userRecord = new UserRecord();
        userRecord.setBodyKey(entry.key);
        userRecord.setType(entry.type);
        userRecord.setOwner(entry.owner);
        userRecord.setSubject(subject);
        return userRecord;
    }
}
