package com.fairandsmart.consent.api.dto;

import com.fairandsmart.consent.manager.entity.ConsentElementVersion;

import javax.validation.constraints.NotNull;

public class UpdateEntryStatusDto {

    @NotNull
    private ConsentElementVersion.Status status;
    @NotNull
    private ConsentElementVersion.Revocation revocation;

    public UpdateEntryStatusDto() {
    }

    public ConsentElementVersion.Status getStatus() {
        return status;
    }

    public void setStatus(ConsentElementVersion.Status status) {
        this.status = status;
    }

    public ConsentElementVersion.Revocation getRevocation() {
        return revocation;
    }

    public void setRevocation(ConsentElementVersion.Revocation revocation) {
        this.revocation = revocation;
    }

    @Override
    public String toString() {
        return "UpdateEntryStatusDto{" +
                "status=" + status +
                ", revocation=" + revocation +
                '}';
    }
}
