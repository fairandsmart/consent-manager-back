package com.fairandsmart.consent.api.dto;

import com.fairandsmart.consent.common.validation.Locale;
import com.fairandsmart.consent.manager.entity.ModelData;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ContentDto {

    @NotNull @Locale
    private String locale;
    @NotNull @Valid
    private ModelData content;

    public ContentDto() {
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
