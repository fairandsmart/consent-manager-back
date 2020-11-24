package com.fairandsmart.consent.api.dto;

import com.fairandsmart.consent.manager.entity.Record;
import com.fairandsmart.consent.manager.entity.Subject;

import java.util.Map;

public class ExtractionResultDto {

    private String subjectId;
    private String subjectName;
    private String subjectEmail;
    private String recordSerial;
    private String recordValue;

    public ExtractionResultDto() {
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

    public static ExtractionResultDto build(Map.Entry<Subject, Record> entry) {
        ExtractionResultDto dto = new ExtractionResultDto();
        dto.setSubjectId(entry.getKey().id);
        dto.setSubjectName(entry.getKey().name);
        dto.setSubjectEmail(entry.getKey().emailAddress);
        dto.setRecordSerial(entry.getValue().serial);
        dto.setRecordValue(entry.getValue().value);
        return dto;
    }
}
