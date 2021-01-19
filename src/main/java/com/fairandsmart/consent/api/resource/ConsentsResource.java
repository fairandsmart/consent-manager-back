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
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.*;
import com.fairandsmart.consent.manager.exception.ConsentServiceException;
import com.fairandsmart.consent.manager.exception.InvalidConsentException;
import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateService;
import com.fairandsmart.consent.template.TemplateServiceException;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;
import com.fairandsmart.consent.token.TokenService;
import com.fairandsmart.consent.token.TokenServiceException;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("consents")
@Tag(name = "Consent", description = "Operations related to consent's collect")
public class ConsentsResource {

    private static final Logger LOGGER = Logger.getLogger(ConsentsResource.class.getName());

    @Inject
    ConsentService consentService;

    @Inject
    TokenService tokenService;

    @Inject
    TemplateService templateService;

    @POST
    @Path("token")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(summary = "Generate a thin token from a given context")
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "thin token has been generated", content = @Content(example = "a token")),
            @APIResponse(responseCode = "401", description = AccessDeniedException.ACCESS_TOKEN_ISSUE)
    })
    public String generateToken(@Valid ConsentContext ctx) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "POST /consents/token");
        return consentService.buildToken(ctx);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Operation(summary = "Generate a form from a given thin token")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "form has been generated", content = @Content(example = "consent form HTML code")),
            @APIResponse(responseCode = "401", description = "thin token is either invalid or missing")
    })
    public TemplateModel<ConsentForm> getFormHtml(@QueryParam("t") @NotNull String token) throws TokenExpiredException, EntityNotFoundException, ConsentServiceException, InvalidTokenException, TemplateServiceException {
        LOGGER.log(Level.INFO, "GET /consents (html)");
        ConsentForm form = consentService.generateForm(token);
        return templateService.buildModel(form);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ConsentForm getFormJson(@QueryParam("t") @NotNull String token) throws TokenExpiredException, EntityNotFoundException, ConsentServiceException, InvalidTokenException {
        LOGGER.log(Level.INFO, "GET /consents (json)");
        return consentService.generateForm(token);
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    @Operation(summary = "Submit a consent form result")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "form result has been recorded", content = @Content(example = "consent receipt HTML code")),
            @APIResponse(responseCode = "401", description = "thin token is either invalid or missing")
    })
    public TemplateModel<ConsentFormResult> postConsent(MultivaluedMap<String, String> values, @Context UriInfo uriInfo
    ) throws AccessDeniedException, TokenExpiredException, InvalidTokenException, InvalidConsentException, ConsentServiceException, TokenServiceException, TemplateServiceException {
        LOGGER.log(Level.INFO, "POST /consents");
        if (!values.containsKey("token")) {
            throw new AccessDeniedException("unable to find token in form");
        }
        ConsentFormResult result = this.internalPostConsent(values, uriInfo);
        return templateService.buildModel(result);
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public ConsentFormResult postConsentJson(MultivaluedMap<String, String> values, @Context UriInfo uriInfo) throws AccessDeniedException, TokenExpiredException, InvalidTokenException, InvalidConsentException, ConsentServiceException, TokenServiceException {
        LOGGER.log(Level.INFO, "POST /consents (json)");
        if (!values.containsKey("token")) {
            throw new AccessDeniedException("unable to find token in form");
        }
        return this.internalPostConsent(values, uriInfo);
    }

    private ConsentFormResult internalPostConsent(MultivaluedMap<String, String> values, UriInfo uriInfo) throws TokenServiceException, TokenExpiredException, InvalidTokenException, ConsentServiceException, InvalidConsentException {
        ConsentTransaction tx = consentService.submitConsent(values.get("token").get(0), values);
        UriBuilder uri = uriInfo.getBaseUriBuilder().path(ReceiptsResource.class).path(tx.getTransaction()).queryParam("t", tokenService.generateToken(tx));
        ConsentContext ctx = (ConsentContext) tokenService.readToken(values.get("token").get(0));
        ConsentFormResult consentFormResult = new ConsentFormResult();
        consentFormResult.setContext(ctx);
        if (ctx.getReceiptDisplayType() != null && ctx.getReceiptDisplayType() != ConsentContext.ReceiptDisplayType.NONE) {
            uri.queryParam("format", ctx.getReceiptDisplayType());
            Optional<ConsentElementIdentifier> themeId = ConsentElementIdentifier.deserialize(ctx.getTheme());
            themeId.ifPresent(consentElementIdentifier -> uri.queryParam("theme", consentElementIdentifier.getKey()));
        }
        consentFormResult.setReceiptURI(uri.build());
        return consentFormResult;
    }

}
