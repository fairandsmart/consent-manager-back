package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.token.TokenService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/admin")
public class AdminResource {

    private static final Logger LOGGER = Logger.getLogger(AdminResource.class.getName());

    @Inject
    TokenService tokenService;

    @POST
    @Path("/token")
    @RolesAllowed("admin")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String generateToken(@Context SecurityContext sec, ConsentContext ctx) {
        LOGGER.log(Level.INFO, "Generating token");
        LOGGER.log(Level.FINE, "Authenticated user : " + sec.getUserPrincipal().getName());
        return tokenService.generateToken(ctx);
    }

}
