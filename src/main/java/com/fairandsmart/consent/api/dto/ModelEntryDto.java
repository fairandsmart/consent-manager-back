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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
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

    public ModelEntryDto() {
    }

    public static ModelEntryDto fromModelEntry(ModelEntry entry, List<ModelVersion> versions) throws ModelDataSerializationException {
        ModelEntryDto dto = new ModelEntryDto();
        dto.setId(entry.id);
        dto.setKey(entry.key);
        dto.setName(entry.name);
        dto.setType(entry.type);
        dto.setDescription(entry.description);
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

    @Override
    public String toString() {
        return "ModelEntryDto{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", versions=" + versions +
                '}';
    }
}
