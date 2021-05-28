package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.manager.SubjectContext;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
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
@Tag(name = "Tokens", description = "Token generation")
public class TokensResource {

    private static final Logger LOGGER = Logger.getLogger(TokensResource.class.getName());

    @Inject
    ConsentService consentService;

    @POST
    @Path("consent")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "The generated token", content = @Content(example = "a token")),
            @APIResponse(responseCode = "401", description = AccessDeniedException.ACCESS_TOKEN_ISSUE)
    })
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "Generate a form token from a given context")
    public String generateFormToken(@Valid ConsentContext ctx) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "POST /tokens/consent");
        return consentService.buildFormToken(ctx);
    }

    @POST
    @Path("subject")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "The generated token"),
            @APIResponse(responseCode = "401", description = AccessDeniedException.NO_OPERATOR_ROLE),
    })
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "Create subject token for accessing API")
    public String createSubjectToken(@Valid SubjectContext ctx) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "POST /tokens/subject");
        return consentService.buildSubjectToken(ctx);
    }

}
