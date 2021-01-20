package com.fairandsmart.consent.api.dto;

/*-
 * #%L
 * Right Consent / A Consent Manager Platform
 *
 * Authors:
 *
 * Xavier Lefevre <xavier.lefevre@fairandsmart.com> / FairAndSmart
 * Nicolas Rueff <nicolas.rueff@fairandsmart.com> / FairAndSmart
 * Jérôme Blanchard <jerome.blanchard@fairandsmart.com> / FairAndSmart
 * Alan Balbo <alan.balbo@fairandsmart.com> / FairAndSmart
 * Frederic Pierre <frederic.pierre@fairansmart.com> / FairAndSmart
 * Victor Guillaume <victor.guillaume@fairandsmart.com> / FairAndSmart
 * Manon Stremplewski <manon.stremplewski@fairandsmart.com> / FairAndSmart
 * Pauline Kullmann <pauline.kullmmann@fairandsmart.com> / FairAndSmart
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.fairandsmart.consent.common.config.ClientConfig;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Schema(name = "ClientConfig", description = "A representation of the client configuration")
public class ClientConfigDto {

    @Schema(description = "Language used in data", readOnly = true)
    private String language;
    @Schema(description = "Is user page need to be available", readOnly = true)
    private boolean userPageEnabled;
    @Schema(description = "Elements (processing, preferences, ...) that are available on user page", readOnly = true)
    private List<String> userPageElements;

    public ClientConfigDto() {
        userPageElements = new ArrayList<>();
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

    @Override
    public String toString() {
        return "ClientConfigDto{" +
                "language='" + language + '\'' +
                ", userPageEnabled=" + userPageEnabled +
                ", userPageElements=" + userPageElements +
                '}';
    }
}
