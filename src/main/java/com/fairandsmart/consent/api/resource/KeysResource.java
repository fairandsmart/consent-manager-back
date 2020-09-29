package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.security.AuthenticationService;
import com.fairandsmart.consent.security.entity.Key;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Path("/keys")
public class KeysResource {

    private static final Logger LOGGER = Logger.getLogger(KeysResource.class.getName());

    @Inject
    AuthenticationService authenticationService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Key> listKeys() throws AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /keys");
        return authenticationService.listKeys();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Key createKey(Key key) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "POST /users/key");
        return authenticationService.createKey(key.name);
    }

    @DELETE
    @Path("/{id}")
    public Response dropKey(@PathParam("id") String id) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "DELETE /users/key/" + id);
        authenticationService.dropKey(id);
        return Response.noContent().build();
    }

}
