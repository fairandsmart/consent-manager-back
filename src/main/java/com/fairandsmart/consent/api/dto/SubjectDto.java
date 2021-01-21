package com.fairandsmart.consent.api.dto;

import com.fairandsmart.consent.common.consts.Placeholders;
import com.fairandsmart.consent.manager.entity.Subject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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

@Schema(description = "Represents a subject")
public class SubjectDto {

    @Schema(description = "The subject ID", readOnly = true, example = Placeholders.NIL_UUID)
    private String id;
    @NotEmpty
    @Size(max = 255)
    @Schema(description = "The subject name", example = Placeholders.SHELDON)
    private String name;
    @Email
    @Schema(description = "The subject email address", example = Placeholders.SHELDON_AT_LOCALHOST)
    private String emailAddress;
    @Schema(description = "The subject creation date (epoch with millisec)", readOnly = true, example = Placeholders.TS_2021_01_01)
    private long creationTimestamp;

    public static SubjectDto fromSubject(Subject subject) {
        SubjectDto dto = new SubjectDto();
        dto.setId(subject.id);
        dto.setName(subject.name);
        dto.setEmailAddress(subject.emailAddress);
        dto.setCreationTimestamp(subject.creationTimestamp);
        return dto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    @Override
    public String toString() {
        return "SubjectDto{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", creationTimestamp=" + creationTimestamp +
                '}';
    }
}
