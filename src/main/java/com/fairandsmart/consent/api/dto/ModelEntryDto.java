package com.fairandsmart.consent.api.dto;

import com.fairandsmart.consent.common.validation.ModelKey;
import com.fairandsmart.consent.manager.entity.ModelEntry;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

    public static ModelEntryDto fromModelEntry(ModelEntry entry) {
        ModelEntryDto dto = new ModelEntryDto();
        dto.setId(entry.id);
        dto.setKey(entry.key);
        dto.setName(entry.name);
        dto.setType(entry.type);
        dto.setDescription(entry.description);
        return dto;
    }
}
