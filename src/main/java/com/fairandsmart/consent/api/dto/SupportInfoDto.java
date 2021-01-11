package com.fairandsmart.consent.api.dto;

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
}
