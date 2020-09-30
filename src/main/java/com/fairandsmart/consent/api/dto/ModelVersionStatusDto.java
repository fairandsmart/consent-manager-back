package com.fairandsmart.consent.api.dto;

import com.fairandsmart.consent.manager.entity.ModelVersion;

import javax.validation.constraints.NotNull;

public class ModelVersionStatusDto {

    @NotNull
    private ModelVersion.Status status;

    public ModelVersionStatusDto() {
    }

    public ModelVersion.Status getStatus() {
        return status;
    }

    public void setStatus(ModelVersion.Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ModelVersionStatusDto{" +
                "status=" + status +
                '}';
    }
}
