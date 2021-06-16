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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Schema(name = "User", description = "A representation of connected user status")
public class UserStatusDto {

    @Schema(description = "The username", readOnly = true)
    private String username;
    private ModelEntryDto infos;
    private List<ModelEntryDto> entries;
    private Map<String, RecordDto> records;

    public UserStatusDto() {
        entries = new ArrayList<>();
        records = new HashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ModelEntryDto getInfos() {
        return infos;
    }

    public void setInfos(ModelEntryDto infos) {
        this.infos = infos;
    }

    public List<ModelEntryDto> getEntries() {
        return entries;
    }

    public void setEntries(List<ModelEntryDto> entries) {
        this.entries = entries;
    }

    public Map<String, RecordDto> getRecords() {
        return records;
    }

    public void setRecords(Map<String, RecordDto> records) {
        this.records = records;
    }
}
