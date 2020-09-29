package com.fairandsmart.consent.api.dto;

import com.fairandsmart.consent.manager.entity.ModelVersion;

import javax.validation.constraints.NotNull;

public class ModelVersionTypeDto {

    @NotNull
    private ModelVersion.Type type;

    public ModelVersionTypeDto() {
    }

    public ModelVersion.Type getType() {
        return type;
    }

    public void setType(ModelVersion.Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ModelVersionTypeDto{" +
                "type=" + type +
                '}';
    }
}
