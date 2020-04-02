package com.fairandsmart.consent.api.dto;

import com.fairandsmart.consent.common.validation.UUID;
import com.fairandsmart.consent.manager.entity.Content;
import com.fairandsmart.consent.manager.entity.Information;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateInformationDto {

    @NotNull
    private Information.Type type;
    @UUID
    private String parent;
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
    @Valid
    @NotNull
    private Content content;

    public CreateInformationDto() {
    }

    public Information.Type getType() {
        return type;
    }

    public void setType(Information.Type type) {
        this.type = type;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
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

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
