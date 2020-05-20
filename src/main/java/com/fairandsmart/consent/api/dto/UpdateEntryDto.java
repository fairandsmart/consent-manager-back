package com.fairandsmart.consent.api.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateEntryDto {

    @NotNull @Size(min = 2, max = 255)
    private String name;
    @Size(max = 2500)
    private String description;

    public UpdateEntryDto() {
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
}
