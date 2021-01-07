package com.fairandsmart.consent.api.resource;

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

import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.validation.UUID;
import com.fairandsmart.consent.security.AuthenticationService;
import com.fairandsmart.consent.security.entity.Key;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    @APIResponses( value = {
        @APIResponse( responseCode = "401", description = "Access Denied (if you don't have 'admin' role)" ),
        @APIResponse( responseCode = "200", description = "The list of all keys" ) })
    @Operation( operationId = "listKeys", summary = "List of all available API access keys")
    public List<Key> listKeys() throws AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /keys");
        return authenticationService.listKeys();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses( value = {
        @APIResponse( responseCode = "401", description = "Access Denied (if you don't have 'admin' role)" ),
        @APIResponse( responseCode = "200", description = "The key has been created") })
    @Operation( operationId = "createKey", summary = "Create a new API access key")
    public Key createKey(Key key) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "POST /users/key");
        return authenticationService.createKey(key.name);
    }

    @DELETE
    @Path("{id}")
    @APIResponses( value = {
        @APIResponse( responseCode = "401", description = "Access Denied (if you don't have 'admin' role)" ),
        @APIResponse( responseCode = "204", description = "The key has been deleted") })
    @Operation( operationId = "deleteKey", summary = "Delete the key with that id")
    public Response deleteKey ( @Parameter(name = "id", description = "The key UUID to delete", example = "6837d1cd-ad9f-4b62-a73f-731c78e8283b") @PathParam("id") @NotNull @NotEmpty @UUID String id) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "DELETE /users/key/" + id);
        authenticationService.dropKey(id);
        return Response.noContent().build();
    }

}
