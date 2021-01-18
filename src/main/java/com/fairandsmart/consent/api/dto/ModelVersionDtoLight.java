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

import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.exception.ModelDataSerializationException;

import java.util.Arrays;
import java.util.List;

public class ModelVersionDtoLight {

    private String id;
    private String serial;
    private String parent;
    private String child;
    private String author;
    private String defaultLanguage;
    private List<String> availableLanguages;
    private ModelVersion.Status status;
    private ModelVersion.Type type;
    private long creationDate;
    private long modificationDate;
    private String identifier;

    public ModelVersionDtoLight() {
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
