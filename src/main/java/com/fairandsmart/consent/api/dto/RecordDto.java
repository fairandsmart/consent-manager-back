package com.fairandsmart.consent.api.dto;

/*-
 * #%L
 * Right Consents, a Universal Consents & Preferences Management Platform.
 * %%
 * Copyright (C) 2020 - 2021 Fair And Smart
 * %%
 * This file is part of Right Consents Community Edition.
 * 
 * Right Consents Community Edition is published by FAIR AND SMART under the
 * GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 * 
 * For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 * files, or see https://www.fairandsmart.com/opensource/.
 * #L%
 */

import com.fairandsmart.consent.manager.entity.Record;
import com.fairandsmart.consent.notification.entity.NotificationReport;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Schema(name = "Record", description = "A Record holds a single user consent or choice")
public class RecordDto {

    @Schema(description = "The record serial composed of info and body serial", readOnly = true)
    private String serial;
    @Schema(description = "The information model key", readOnly = true)
    private String infoKey;
    @Schema(description = "The body model key (can be a processing, a preference or a condition", readOnly = true)
    private String bodyKey;
    @Schema(description = "The subject id that record belong to", readOnly = true)
    private String subject;
    @Schema(description = "The creation timestamp", readOnly = true)
    private long creationTimestamp;
    @Schema(description = "The expiration timestamp", readOnly = true)
    private long expirationTimestamp;
    @Schema(description = "The record type", readOnly = true)
    private String type;
    @Schema(description = "The user value", readOnly = true)
    private String value;
    @Schema(description = "The record status after evaluation", readOnly = true)
    private String status;
    @Schema(description = "The record evaluation explanation", readOnly = true)
    private String statusExplanation;
    @Schema(description = "The collection method of that record", readOnly = true)
    private String collectionMethod;
    @Schema(description = "A comment added to the record", readOnly = true)
    private String comment;
    @Schema(description = "The record transaction id (used also as the receipt id)", readOnly = true)
    private String transaction;
    @Schema(description = "A list of NotificationReport holding notification status", readOnly = true)
    private List<NotificationReport> notificationReports;

    public RecordDto() {
    }

    public static RecordDto fromRecord(Record record) {
        RecordDto dto = new RecordDto();
        dto.setSubject(record.subject);
        dto.setSerial(record.serial);
        dto.setInfoKey(record.infoKey);
        dto.setBodyKey(record.bodyKey);
        dto.setCreationTimestamp(record.creationTimestamp);
        dto.setExpirationTimestamp(record.expirationTimestamp);
        dto.setCollectionMethod(record.collectionMethod.toString());
        dto.setType(record.type);
        dto.setComment(record.comment);
        dto.setStatus(record.status.toString());
        dto.setStatusExplanation(record.statusExplanation.toString());
        dto.setValue(record.value);
        dto.setTransaction(record.transaction);
        return dto;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public String getBodyKey() {
        return bodyKey;
    }

    public void setBodyKey(String bodyKey) {
        this.bodyKey = bodyKey;
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

    public String getStatusExplanation() {
        return statusExplanation;
    }

    public void setStatusExplanation(String statusExplanation) {
        this.statusExplanation = statusExplanation;
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

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public List<NotificationReport> getNotificationReports() {
        return notificationReports;
    }

    public void setNotificationReports(List<NotificationReport> notificationReports) {
        this.notificationReports = notificationReports;
    }

    public RecordDto withNotificationReports(List<NotificationReport> notificationReports) {
        this.notificationReports = notificationReports;
        return this;
    }

    @Override
    public String toString() {
        return "RecordDto{" +
                "serial='" + serial + '\'' +
                ", infoKey='" + infoKey + '\'' +
                ", bodyKey='" + bodyKey + '\'' +
                ", subject='" + subject + '\'' +
                ", creationTimestamp=" + creationTimestamp +
                ", expirationTimestamp=" + expirationTimestamp +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", status='" + status + '\'' +
                ", statusExplanation='" + statusExplanation + '\'' +
                ", collectionMethod='" + collectionMethod + '\'' +
                ", comment='" + comment + '\'' +
                ", transaction='" + transaction + '\'' +
                ", notificationReports=" + notificationReports +
                '}';
    }
}
