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
