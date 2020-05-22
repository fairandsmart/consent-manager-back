package com.fairandsmart.consent.api.dto;

import com.fairandsmart.consent.common.validation.Locale;
import com.fairandsmart.consent.manager.entity.ConsentElementData;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class UpdateEntryContentDto {

    @NotNull @Locale
    private String locale;
    @NotNull @Valid
    private ConsentElementData content;

    public UpdateEntryContentDto() {
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
