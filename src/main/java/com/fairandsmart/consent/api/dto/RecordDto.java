package com.fairandsmart.consent.api.dto;

/*-
 * #%L
 * Right Consent / A Consent Manager Platform
 * 
 * Authors:
 * 
 * Xavier Lefevre <xavier.lefevre@fairandsmart.com> / FairAndSmart
 * Nicolas Rueff <nicolas.rueff@fairandsmart.com> / FairAndSmart
 * Jérôme Blanchard <jerome.blanchard@fairandsmart.com> / FairAndSmart
 * Alan Balbo <alan.balbo@fairandsmart.com> / FairAndSmart
 * Frederic Pierre <frederic.pierre@fairansmart.com> / FairAndSmart
 * Victor Guillaume <victor.guillaume@fairandsmart.com> / FairAndSmart
 * Manon Stremplewski <manon.stremplewski@fairandsmart.com> / FairAndSmart
 * Pauline Kullmann <pauline.kullmmann@fairandsmart.com> / FairAndSmart
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.fairandsmart.consent.manager.entity.Record;
import com.fairandsmart.consent.notification.entity.NotificationReport;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Schema(name="Record", description="A Record holds a single user consent or choice")
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
