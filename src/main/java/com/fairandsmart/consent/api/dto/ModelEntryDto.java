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

import com.fairandsmart.consent.common.validation.ModelKey;
import com.fairandsmart.consent.manager.entity.ModelEntry;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.exception.ModelDataSerializationException;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelEntryDto {

    private String id;
    @NotNull
    @ModelKey
    private String key;
    @NotNull
    @Size(min = 2, max = 255)
    private String name;
    @Size(max = 2500)
    private String description;
    @NotNull
    private String type;
    private List<ModelVersionDtoLight> versions = new ArrayList<>();
    private long creationDate;
    private long modificationDate;
    @Enumerated(EnumType.STRING)
    private ModelEntry.Status status;
    private String defaultLanguage;
    private List<String> availableLanguages = new ArrayList<>();

    public ModelEntryDto() {
    }

    public static ModelEntryDto fromModelEntry(ModelEntry entry, List<ModelVersion> versions) throws ModelDataSerializationException {
        ModelEntryDto dto = new ModelEntryDto();
        dto.setId(entry.id);
        dto.setKey(entry.key);
        dto.setName(entry.name);
        dto.setType(entry.type);
        dto.setDescription(entry.description);
        dto.setCreationDate(entry.creationDate);
        dto.setModificationDate(entry.modificationDate);
        dto.setStatus(entry.status);
        dto.setDefaultLanguage(entry.defaultLanguage);
        if (entry.availableLanguages != null && entry.availableLanguages.length() > 0) {
            dto.setAvailableLanguages(Arrays.asList(entry.availableLanguages.split(",")));
        }
        List<ModelVersionDtoLight> lightVersions = new ArrayList<>();
        for (ModelVersion version : versions) {
            ModelVersionDtoLight modelVersionDtoLight = ModelVersionDtoLight.fromModelVersion(version);
            lightVersions.add(modelVersionDtoLight);
        }
        dto.setVersions(lightVersions);
        return dto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ModelVersionDtoLight> getVersions() {
        return versions;
    }

    public void setVersions(List<ModelVersionDtoLight> versions) {
        this.versions = versions;
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

    public ModelEntry.Status getStatus() {
        return status;
    }

    public void setStatus(ModelEntry.Status status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "ModelEntryDto{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", versions=" + versions +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                ", status=" + status +
                ", defaultLanguage='" + defaultLanguage + '\'' +
                ", availableLanguages=" + availableLanguages +
                '}';
    }

}
