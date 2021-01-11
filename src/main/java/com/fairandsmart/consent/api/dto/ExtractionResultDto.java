package com.fairandsmart.consent.api.dto;

import com.fairandsmart.consent.manager.entity.Record;
import com.fairandsmart.consent.manager.entity.Subject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Map;

@Schema(name="Extraction Result", description="A Record Extraction Result")
public class ExtractionResultDto {

    @Schema(description = "The record subject id", readOnly = true)
    private String subjectId;
    @Schema(description = "The record subject name (if present)", readOnly = true)
    private String subjectName;
    @Schema(description = "The record subject email (if present)", readOnly = true)
    private String subjectEmail;
    @Schema(description = "The record key", readOnly = true)
    private String recordKey;
    @Schema(description = "The record serial", readOnly = true)
    private String recordSerial;
    @Schema(description = "The record value", readOnly = true)
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
}
