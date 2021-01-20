package com.fairandsmart.consent.api.dto;

import com.fairandsmart.consent.common.consts.Placeholders;
import com.fairandsmart.consent.manager.entity.Subject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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
