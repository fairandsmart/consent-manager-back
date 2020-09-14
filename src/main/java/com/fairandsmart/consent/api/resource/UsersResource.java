package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.api.dto.UserDto;
import com.fairandsmart.consent.security.AuthenticationService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/users")
public class UsersResource {

    private static final Logger LOGGER = Logger.getLogger(UsersResource.class.getName());

    @Inject
    private AuthenticationService authenticationService;

    @GET
    @Path("/me")
    @Produces(MediaType.APPLICATION_JSON)
    public UserDto me() {
        LOGGER.log(Level.INFO, "GET /users/me");
        UserDto user = new UserDto();
        user.setUsername(authenticationService.getConnectedIdentifier());
        user.setAdmin(authenticationService.isConnectedIdentifierAdmin());
        user.setOperator(authenticationService.isConnectedIdentifierOperator());
        user.setRoles(authenticationService.listConnectedIdentifierRoles());
        return user;
    }

}
