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

import com.fairandsmart.consent.common.consts.Placeholders;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.validation.UUID;
import com.fairandsmart.consent.security.AuthenticationService;
import com.fairandsmart.consent.security.entity.Key;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/keys")
@Tag(name = "Keys", description = "Operations related to API access keys")
public class KeysResource {

    private static final Logger LOGGER = Logger.getLogger(KeysResource.class.getName());

    @Inject
    AuthenticationService authenticationService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "401", description = AccessDeniedException.ACCESS_TOKEN_ISSUE),
            @APIResponse(responseCode = "200", description = "The list of all keys")
    })
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "List of all available API access keys")
    public List<Key> listKeys() throws AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /keys");
        return authenticationService.listKeys();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "401", description = AccessDeniedException.ACCESS_TOKEN_ISSUE),
            @APIResponse(responseCode = "200", description = "The created key")})
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "Create a new API access key")
    public Key createKey(Key key) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "POST /users/key");
        return authenticationService.createKey(key.name);
    }

    @DELETE
    @Path("{id}")
    @APIResponses(value = {
            @APIResponse(responseCode = "401", description = AccessDeniedException.ACCESS_TOKEN_ISSUE),
            @APIResponse(responseCode = "204", description = "The key has been deleted")})
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "Delete the key with that id")
    public Response deleteKey(
            @Parameter(description = "The key UUID (NOT its name) to delete", example = Placeholders.NIL_UUID) @PathParam("id") @NotEmpty @UUID String id) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "DELETE /users/key/{}", id);
        authenticationService.dropKey(id);
        return Response.noContent().build();
    }

}
