package com.fairandsmart.consent.api.dto;

/*-
 * #%L
 * Right Consents Community Edition
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * You accept that the Program was not created with a view to satisfy Your
 * individual requirements. Therefore, you must ensure that the Program
 * comply with Your needs, requirements and constraints. FAIR AND SMART
 * represents and warrants that it holds, without any restriction or
 * reservation, all the legal titles, authorizations and intellectual
 * property rights granted in the context of the GPLv3 License. See the
 * Additional Terms for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <https://www.gnu.org/licenses/>.
 * 
 * You should have received a copy of the Additional Terms along with this
 * program. If not, see <https://www.fairandsmart.com/opensource/>.
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
