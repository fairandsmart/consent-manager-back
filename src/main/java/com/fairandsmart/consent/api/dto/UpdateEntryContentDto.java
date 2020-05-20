package com.fairandsmart.consent.api.dto;

import com.fairandsmart.consent.common.validation.Locale;
import com.fairandsmart.consent.common.validation.ModelKey;
import com.fairandsmart.consent.manager.entity.ConsentElementData;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class UpdateEntryContentDto {

    @NotNull
    @ModelKey
    private String key;
    @NotNull @Locale
    private String locale;
    @NotNull @Valid
    private ConsentElementData content;

    public UpdateEntryContentDto() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public ConsentElementData getContent() {
        return content;
    }

    public void setContent(ConsentElementData content) {
        this.content = content;
    }

}
