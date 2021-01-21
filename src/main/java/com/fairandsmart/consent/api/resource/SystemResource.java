package com.fairandsmart.consent.api.resource;

/*-
 * #%L
 * Right Consents Community Edition
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * You accept that the Program was not created with a view to satisfy Your
 * individual requirements. Therefore, you must ensure that the Program
 * comply with Your needs, requirements and constraints. FAIR AND SMART
 * represents and warrants that it holds, without any restriction or
 * reservation, all the legal titles, authorizations and intellectual
 * property rights granted in the context of the GPLv3 License. See the
 * Additional Terms for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <https://www.gnu.org/licenses/>.
 * 
 * You should have received a copy of the Additional Terms along with this
 * program. If not, see <https://www.fairandsmart.com/opensource/>.
 * #L%
 */

import com.fairandsmart.consent.api.dto.ClientConfigDto;
import com.fairandsmart.consent.api.dto.SupportInfoDto;
import com.fairandsmart.consent.api.dto.UserDto;
import com.fairandsmart.consent.common.config.ClientConfig;
import com.fairandsmart.consent.security.AuthenticationService;
import com.fairandsmart.consent.support.SupportService;
import com.fairandsmart.consent.support.SupportServiceException;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/system")
@Tag(name = "System", description = "System operations")
public class SystemResource {

    private static final Logger LOGGER = Logger.getLogger(SystemResource.class.getName());

    @Inject
    AuthenticationService authenticationService;

    @Inject
    SupportService supportService;

    @Inject
    ClientConfig config;

    @GET
    @Path("/users/me")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", description = "Information for the connected user")
    @Operation(summary = "Get the connected user information")
    public UserDto me() {
        LOGGER.log(Level.INFO, "GET /system/users/me");
        UserDto user = new UserDto();
        user.setUsername(authenticationService.getConnectedIdentifier());
        user.setAdmin(authenticationService.isConnectedIdentifierAdmin());
        user.setOperator(authenticationService.isConnectedIdentifierOperator());
        user.setRoles(authenticationService.listConnectedIdentifierRoles());
        return user;
    }

    @GET
    @Path("/support/infos")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "Support service is disabled"),
            @APIResponse(responseCode = "200", description = "Support service information")})
    @Operation(summary = "Get available service information (for now, only latest version is operational)")
    public SupportInfoDto supportInfos() throws SupportServiceException {
        LOGGER.log(Level.INFO, "GET /system/support/infos");
        SupportInfoDto dto = new SupportInfoDto();
        dto.setStatus(supportService.getSupportStatus());
        dto.setLatestVersion(supportService.getLatestVersion());
        dto.setCurrentVersion(supportService.getCurrentVersion());
        return dto;
    }

    @GET
    @Path("/config")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", description = "Configuration for GUI")
    @Operation(summary = "Get the GUI configuration")
    public ClientConfigDto getClientConfig() {
        LOGGER.log(Level.INFO, "GET /system/config");
        ClientConfigDto dto = ClientConfigDto.fromClientConfig(config);
        dto.setLanguage(supportService.getInstance().language);
        return dto;
    }

}
