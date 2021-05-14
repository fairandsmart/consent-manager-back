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

import com.fairandsmart.consent.common.config.ClientConfig;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.*;

@Schema(name = "ClientConfig", description = "A representation of the client configuration")
public class ClientConfigDto {

    @Schema(description = "Language used in data", readOnly = true)
    private String language;
    @Schema(description = "Is user page need to be available", readOnly = true)
    private boolean userPageEnabled;
    @Schema(description = "Elements (processing, preferences, ...) that are available on user page", readOnly = true)
    private List<String> userPageElements;
    @Schema(description = "Roles mapping", readOnly = true)
    private Map<String, String> rolesMapping;


    public ClientConfigDto() {
        userPageElements = new ArrayList<>();
        rolesMapping = new HashMap<>();
    }

    public static ClientConfigDto fromClientConfig(ClientConfig config) {
        ClientConfigDto dto = new ClientConfigDto();
        dto.setUserPageEnabled(config.isUserPageEnabled());
        if (config.userPageElements().isPresent()) {
            dto.setUserPageElements(Arrays.asList(config.userPageElements().get().split(",")));
        }
        return dto;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isUserPageEnabled() {
        return userPageEnabled;
    }

    public void setUserPageEnabled(boolean userPageEnabled) {
        this.userPageEnabled = userPageEnabled;
    }

    public List<String> getUserPageElements() {
        return userPageElements;
    }

    public void setUserPageElements(List<String> userPageElements) {
        this.userPageElements = userPageElements;
    }

    public Map<String, String> getRolesMapping() {
        return rolesMapping;
    }

    public void setRolesMapping(Map<String, String> rolesMapping) {
        this.rolesMapping = rolesMapping;
    }

    public void addSecurityRole(String name, String mapping) {
        this.rolesMapping.put(name, mapping);
    }

    @Override
    public String toString() {
        return "ClientConfigDto{" +
                "language='" + language + '\'' +
                ", userPageEnabled=" + userPageEnabled +
                ", userPageElements=" + userPageElements +
                ", rolesMapping=" + rolesMapping +
                '}';
    }
}
