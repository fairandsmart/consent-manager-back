package com.fairandsmart.consent.api.resource;

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
        LOGGER.log(Level.INFO, "POST /keys");
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
        LOGGER.log(Level.INFO, "DELETE /keys/" + id);
        authenticationService.dropKey(id);
        return Response.noContent().build();
    }

}
