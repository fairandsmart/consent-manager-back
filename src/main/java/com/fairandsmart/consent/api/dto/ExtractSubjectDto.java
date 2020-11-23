package com.fairandsmart.consent.api.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ExtractSubjectDto {

    @NotNull
    @NotEmpty
    private String key;
    @NotNull
    @NotEmpty
    private String value;

    public ExtractSubjectDto() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
