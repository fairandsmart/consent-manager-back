package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.token.TokenService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/admin")
public class AdminResource {

    private static final Logger LOGGER = Logger.getLogger(AdminResource.class.getName());

    @Inject
    TokenService tokenService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String generateToken(ConsentContext ctx) {
        LOGGER.log(Level.INFO, "Generating token");
        return tokenService.generateToken(ctx);
    }

}
