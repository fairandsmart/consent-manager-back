package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.token.AccessToken;
import com.fairandsmart.consent.token.TokenService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("tokens")
@Tag(name = "Tokens", description = "Access Token generation")
public class TokensResource {

    private static final Logger LOGGER = Logger.getLogger(TokensResource.class.getName());

    @Inject
    TokenService tokenService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "The generated token"),
            @APIResponse(responseCode = "401", description = AccessDeniedException.NO_OPERATOR_ROLE),
    })
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "Create Access token for API")
    public String createToken(@Valid AccessToken token) {
        LOGGER.log(Level.INFO, "POST /tokens");
        //TODO Handle token creation security
        return tokenService.generateToken(token);
    }

}
