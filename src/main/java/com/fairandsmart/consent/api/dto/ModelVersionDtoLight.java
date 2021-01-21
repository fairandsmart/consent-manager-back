package com.fairandsmart.consent.api.dto;

/*-
 * #%L
 * Right Consents / A Consent Manager Platform
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
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.exception.ModelDataSerializationException;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Arrays;
import java.util.List;

public class ModelVersionDtoLight {

    @Schema(description = "the version UUID", readOnly = true)
    private String id;
    @Schema(description = "the version serial", readOnly = true)
    private String serial;
    @Schema(description = "the previous version UUID", readOnly = true)
    private String parent;
    @Schema(description = "the next version child", readOnly = true)
    private String child;
    @Schema(description = "the version author", readOnly = true, example = "demo")
    private String author;
    @Schema(description = "the version default language", example = "fr")
    private String defaultLanguage;
    @Schema(description = "the version default language", example = "[\"fr\"]")
    private List<String> availableLanguages;
    @Schema(readOnly = true)
    private ModelVersion.Status status;
    @Schema(readOnly = true)
    private ModelVersion.Type type;
    @Schema(description = "the version creation date (epoch in millisec)", readOnly = true, example = Placeholders.TS_2021_01_01)
    private long creationDate;
    @Schema(description = "the version last modification date (epoch in millisec)", readOnly = true, example = Placeholders.TS_2021_01_01)
    private long modificationDate;
    private String identifier; // TODO : document this

    public ModelVersionDtoLight() {
    }

    public static ModelVersionDtoLight fromModelVersion(ModelVersion version) throws ModelDataSerializationException {
        ModelVersionDtoLight dto = new ModelVersionDtoLight();
        dto.setId(version.id);
        dto.setAuthor(version.author);
        dto.setParent(version.parent);
        dto.setChild(version.child);
        dto.setSerial(version.serial);
        dto.setDefaultLanguage(version.defaultLanguage);
        dto.setAvailableLanguages(Arrays.asList(version.availableLanguages.split(",")));
        dto.setStatus(version.status);
        dto.setType(version.type);
        dto.setCreationDate(version.creationDate);
        dto.setModificationDate(version.modificationDate);
        dto.setIdentifier(version.getIdentifier().toString());
        return dto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public List<String> getAvailableLanguages() {
        return availableLanguages;
    }

    public void setAvailableLanguages(List<String> availableLanguages) {
        this.availableLanguages = availableLanguages;
    }

    public ModelVersion.Status getStatus() {
        return status;
    }

    public void setStatus(ModelVersion.Status status) {
        this.status = status;
    }

    public ModelVersion.Type getType() {
        return type;
    }

    public void setType(ModelVersion.Type type) {
        this.type = type;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public long getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(long modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "ModelVersionDtoLight{" +
                "id='" + id + '\'' +
                ", serial='" + serial + '\'' +
                ", parent='" + parent + '\'' +
                ", child='" + child + '\'' +
                ", author='" + author + '\'' +
                ", defaultLanguage='" + defaultLanguage + '\'' +
                ", availableLanguages=" + availableLanguages +
                ", status=" + status +
                ", type=" + type +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                ", identifier='" + identifier + '\'' +
                '}';
    }
}
