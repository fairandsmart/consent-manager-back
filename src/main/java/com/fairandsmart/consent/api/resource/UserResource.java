package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.api.dto.UserDto;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.security.AuthenticationService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("user")
@Tag(name = "User", description = "Connected user resource")
public class UserResource {

    private static final Logger LOGGER = Logger.getLogger(UserResource.class.getName());

    @Inject
    AuthenticationService authenticationService;

    @Inject
    ConsentService consentService;

    @GET
    @Path("me")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", description = "Information for the connected user")
    @Operation(summary = "Get connected user information")
    public UserDto me() {
        LOGGER.log(Level.INFO, "GET /user/me");
        UserDto user = new UserDto();
        user.setUsername(authenticationService.getConnectedIdentifier());
        user.setAdmin(authenticationService.isConnectedIdentifierAdmin());
        user.setOperator(authenticationService.isConnectedIdentifierOperator());
        return user;
    }

    @GET
    @Path("records")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "The users records")
    })
    @Operation(summary = "Get connected user records")
    public Response records(@Context UriInfo info) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /user/records");
        URI other = info.getBaseUriBuilder().replacePath(null).path(RecordsResource.class).replaceQueryParam("subject", authenticationService.getConnectedIdentifier()).build();
        return Response.seeOther(other).build();
    }

}
