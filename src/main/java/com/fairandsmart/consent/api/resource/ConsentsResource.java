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

import com.fairandsmart.consent.common.config.MainConfig;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.manager.*;
import com.fairandsmart.consent.manager.exception.ConsentServiceException;
import com.fairandsmart.consent.manager.exception.GenerateFormException;
import com.fairandsmart.consent.manager.exception.InvalidValuesException;
import com.fairandsmart.consent.manager.exception.SubmitConsentException;
import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateService;
import com.fairandsmart.consent.template.TemplateServiceException;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;
import com.fairandsmart.consent.token.TokenService;
import com.fairandsmart.consent.token.TokenServiceException;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("consents")
@Tag(name = "Consent", description = "Operations related to consent's collect")
public class ConsentsResource {

    private static final Logger LOGGER = Logger.getLogger(ConsentsResource.class.getName());

    @Inject
    MainConfig config;

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
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "thin token has been generated", content = @Content(example = "a token")),
            @APIResponse(responseCode = "401", description = AccessDeniedException.ACCESS_TOKEN_ISSUE)
    })
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "Generate a thin token from a given context")
    public String generateToken(@Valid ConsentContext ctx) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "POST /consents/token");
        return consentService.buildToken(ctx);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "form has been generated", content = @Content(example = "consent form HTML code")),
            @APIResponse(responseCode = "401", description = "thin token is either invalid or missing")
    })
    @Operation(summary = "Generate a form from a given thin token")
    public TemplateModel getFormHtml(@QueryParam("t") @NotNull String token, @HeaderParam( "Accept-Language" ) String acceptLanguage) throws TemplateServiceException {
        LOGGER.log(Level.INFO, "GET /consents (html)");
        try {
            ConsentForm form = consentService.generateForm(token);
            return templateService.buildModel(form);
        } catch (GenerateFormException | TokenExpiredException | InvalidTokenException | ConsentServiceException e) {
            ConsentFormError error = new ConsentFormError(e);
            error.setLanguage(getLanguage(acceptLanguage));
            return templateService.buildModel(error);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ConsentForm getFormJson(@QueryParam("t") @NotNull String token) throws TokenExpiredException, ConsentServiceException, InvalidTokenException, GenerateFormException {
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
    public TemplateModel postConsent(MultivaluedMap<String, String> values, @Context UriInfo uriInfo, @HeaderParam( "Accept-Language" ) String acceptLanguage) throws TemplateServiceException {
        LOGGER.log(Level.INFO, "POST /consents");
        try {
            if (!values.containsKey("token")) {
                throw new AccessDeniedException("unable to find token in form");
            }
            ConsentFormResult result = this.internalPostConsent(values, uriInfo);
            return templateService.buildModel(result);
        } catch (AccessDeniedException | TokenExpiredException | InvalidTokenException | ConsentServiceException | TokenServiceException | SubmitConsentException e) {
            ConsentFormError error = new ConsentFormError(e);
            error.setLanguage(getLanguage(acceptLanguage));
            return templateService.buildModel(error);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public ConsentFormResult postConsentJson(MultivaluedMap<String, String> values, @Context UriInfo uriInfo) throws AccessDeniedException, TokenExpiredException, InvalidTokenException, InvalidValuesException, ConsentServiceException, TokenServiceException, SubmitConsentException {
        LOGGER.log(Level.INFO, "POST /consents (json)");
        if (!values.containsKey("token")) {
            throw new AccessDeniedException("unable to find token in form");
        }
        return this.internalPostConsent(values, uriInfo);
    }

    private ConsentFormResult internalPostConsent(MultivaluedMap<String, String> values, UriInfo uriInfo) throws TokenServiceException, TokenExpiredException, InvalidTokenException, ConsentServiceException, SubmitConsentException {
        ConsentTransaction tx = consentService.submitConsent(values.get("token").get(0), values);
        UriBuilder uri = uriInfo.getBaseUriBuilder().path(ReceiptsResource.class).path(tx.getTransaction()).queryParam("t", tokenService.generateToken(tx));
        ConsentContext ctx = (ConsentContext) tokenService.readToken(values.get("token").get(0));
        ConsentFormResult consentFormResult = new ConsentFormResult();
        consentFormResult.setContext(ctx);
        if (StringUtils.isNotEmpty(ctx.getLayoutData().getDesiredReceiptMimeType())) {
            uri.queryParam("format", ctx.getLayoutData().getDesiredReceiptMimeType());
            Optional<ConsentElementIdentifier> themeId = ConsentElementIdentifier.deserialize(ctx.getLayoutData().getTheme());
            themeId.ifPresent(consentElementIdentifier -> uri.queryParam("theme", consentElementIdentifier.getKey()));
        }
        consentFormResult.setReceiptURI(uri.build());
        return consentFormResult;
    }

    private String getLanguage(String acceptLanguage) {
        LOGGER.log(Level.FINE, "extracting language from header Accept-Language: " + acceptLanguage);
        return (StringUtils.isNotEmpty(acceptLanguage))?new Locale(acceptLanguage).getLanguage():config.language();
    }

}
