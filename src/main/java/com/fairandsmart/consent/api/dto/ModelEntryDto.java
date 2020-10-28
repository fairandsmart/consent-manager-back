package com.fairandsmart.consent.api.dto;

import com.fairandsmart.consent.common.validation.ModelKey;
import com.fairandsmart.consent.manager.ModelDataSerializationException;
import com.fairandsmart.consent.manager.entity.ModelEntry;
import com.fairandsmart.consent.manager.entity.ModelVersion;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class ModelEntryDto {

    private String id;
    @NotNull @ModelKey
    private String key;
    @NotNull @Size(min = 2, max = 255)
    private String name;
    @Size(max = 2500)
    private String description;
    @NotNull
    private String type;
    private List<ModelVersionDtoLight> versions = new ArrayList<>();

    public ModelEntryDto() {
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

    public static ModelEntryDto fromModelEntryWithoutVersions(ModelEntry entry) {
        ModelEntryDto dto = new ModelEntryDto();
        dto.setId(entry.id);
        dto.setKey(entry.key);
        dto.setName(entry.name);
        dto.setType(entry.type);
        dto.setDescription(entry.description);
        return dto;
    }
}
