package com.fairandsmart.consent.api.dto;

import com.fairandsmart.consent.common.validation.Locale;
import com.fairandsmart.consent.common.validation.ModelKey;
import com.fairandsmart.consent.manager.data.ModelData;
import com.fairandsmart.consent.manager.entity.ModelEntry;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateModelEntryDto {

    @NotNull
    private ModelEntry.Type type;
    @NotNull @ModelKey
    private String key;
    @NotNull @Size(min = 2, max = 255)
    private String name;
    @Size(max = 2500)
    private String description;
    @NotNull @Locale
    private String locale;
    @NotNull @Valid
    private ModelData content;

    public CreateModelEntryDto() {
    }

    public ModelEntry.Type getType() {
        return type;
    }

    public void setType(ModelEntry.Type type) {
        this.type = type;
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

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public ModelData getContent() {
        return content;
    }

    public void setContent(ModelData content) {
        this.content = content;
    }


}
