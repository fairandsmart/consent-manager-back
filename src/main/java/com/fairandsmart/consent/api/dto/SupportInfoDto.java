package com.fairandsmart.consent.api.dto;

/*-
 * #%L
 * Right Consents, a Universal Consents & Preferences Management Platform.
 * %%
 * Copyright (C) 2020 - 2021 Fair And Smart
 * %%
 * This file is part of Right Consents Community Edition.
 * 
 * Right Consents Community Edition is published by FAIR AND SMART under the
 * GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 * 
 * For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 * files, or see https://www.fairandsmart.com/opensource/.
 * #L%
 */

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="SupportInfo", description="A representation of available support info")
public class SupportInfoDto {

    @Schema(description = "The support configuration status (enabled or disabled)", readOnly = true)
    private String status;
    @Schema(description = "The latest version available)", readOnly = true)
    private String latestVersion;
    @Schema(description = "The current running version", readOnly = true)
    private String currentVersion;

    public SupportInfoDto() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    @Override
    public String toString() {
        return "SupportInfoDto{" +
                "status='" + status + '\'' +
                ", latestVersion='" + latestVersion + '\'' +
                ", currentVersion='" + currentVersion + '\'' +
                '}';
    }
}
