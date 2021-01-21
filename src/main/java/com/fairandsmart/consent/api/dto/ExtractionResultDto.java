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

import com.fairandsmart.consent.common.consts.Placeholders;
import com.fairandsmart.consent.manager.entity.Record;
import com.fairandsmart.consent.manager.entity.Subject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Map;

@Schema(description = "A Record Extraction Result")
public class ExtractionResultDto {

    @Schema(description = "The record subject id", readOnly = true, example = Placeholders.NIL_UUID)
    private String subjectId;
    @Schema(description = "The record subject name (if present)", readOnly = true, example = Placeholders.SHELDON)
    private String subjectName;
    @Schema(description = "The record subject email (if present)", readOnly = true, example = Placeholders.SHELDON_AT_LOCALHOST)
    private String subjectEmail;
    @Schema(description = "The record key", readOnly = true, example = "processing.001")
    private String recordKey;
    @Schema(description = "The record serial", readOnly = true, example = "0000000")
    private String recordSerial;
    @Schema(description = "The record value", readOnly = true, example = "accepted")
    private String recordValue;

    public ExtractionResultDto() {
    }

    public static ExtractionResultDto build(Map.Entry<Subject, Record> entry) {
        ExtractionResultDto dto = new ExtractionResultDto();
        dto.setSubjectId(entry.getKey().id);
        dto.setSubjectName(entry.getKey().name);
        dto.setSubjectEmail(entry.getKey().emailAddress);
        dto.setRecordKey(entry.getValue().bodyKey);
        dto.setRecordSerial(entry.getValue().serial);
        dto.setRecordValue(entry.getValue().value);
        return dto;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectEmail() {
        return subjectEmail;
    }

    public void setSubjectEmail(String subjectEmail) {
        this.subjectEmail = subjectEmail;
    }

    public String getRecordKey() {
        return recordKey;
    }

    public void setRecordKey(String recordKey) {
        this.recordKey = recordKey;
    }

    public String getRecordSerial() {
        return recordSerial;
    }

    public void setRecordSerial(String recordSerial) {
        this.recordSerial = recordSerial;
    }

    public String getRecordValue() {
        return recordValue;
    }

    public void setRecordValue(String recordValue) {
        this.recordValue = recordValue;
    }

    @Override
    public String toString() {
        return "ExtractionResultDto{" +
                "subjectId='" + subjectId + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", subjectEmail='" + subjectEmail + '\'' +
                ", recordKey='" + recordKey + '\'' +
                ", recordSerial='" + recordSerial + '\'' +
                ", recordValue='" + recordValue + '\'' +
                '}';
    }
}
