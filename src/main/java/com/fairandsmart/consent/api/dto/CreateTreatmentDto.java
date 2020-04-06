package com.fairandsmart.consent.api.dto;

import com.fairandsmart.consent.common.validation.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateTreatmentDto {

    @UUID
    private String parent;
    @NotNull
    @Size(min = 3, max = 255)
    private String key;
    @NotNull
    @Size(min = 3, max = 255)
    private String name;
    @NotNull
    @Size(max = 2500)
    private String description;
    @NotNull
    private String defaultLanguage;
    @NotNull
    private String country;

    public CreateTreatmentDto() {
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
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

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "CreateTreatmentDto{" +
                "parent='" + parent + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", defaultLanguage='" + defaultLanguage + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
