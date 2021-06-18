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

import com.fairandsmart.consent.api.dto.TransactionDto;
import com.fairandsmart.consent.common.config.MainConfig;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.UnexpectedException;
import com.fairandsmart.consent.manager.*;
import com.fairandsmart.consent.manager.entity.Transaction;
import com.fairandsmart.consent.manager.exception.ConsentContextSerializationException;
import com.fairandsmart.consent.manager.exception.ConsentServiceException;
import com.fairandsmart.consent.manager.exception.GenerateFormException;
import com.fairandsmart.consent.template.TemplateService;
import com.fairandsmart.consent.token.AccessToken;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
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
    @Produces(MediaType.APPLICATION_JSON)
    public TransactionDto createTransactionJson(@Valid ConsentContext ctx, @Context UriInfo uriInfo) throws AccessDeniedException, ConsentContextSerializationException {
        LOGGER.log(Level.INFO, "POST /consents (json)");
        Transaction tx = consentService.createTransaction(ctx);
        TransactionDto dto = TransactionDto.fromTransaction(tx);
        //TODO Ensure that a SubjectToken is the right one because validity is long (1 year) maybe a TransactionToken (or a new FormToken) could be better
        String token = tokenService.generateToken(new AccessToken().withSubject(dto.getSubject()));
        URI form = uriInfo.getBaseUriBuilder().path(ConsentsResource.class).path(dto.getId()).path("submit").queryParam("t", token).build();
        dto.setFormURI(form);
        return dto;
    }

    @GET
    @Path("{txid}")
    @Produces(MediaType.APPLICATION_JSON)
    public TransactionDto getTransaction(@PathParam("txid") String txid, @Context UriInfo uriInfo) throws AccessDeniedException, EntityNotFoundException, ConsentContextSerializationException {
        LOGGER.log(Level.INFO, "GET /consents/" + txid + " (json)");
        Transaction tx = consentService.getTransaction(txid);
        TransactionDto dto = TransactionDto.fromTransaction(tx);
        //TODO Depending on the transaction state, the action should be another
        String token = tokenService.generateToken(new AccessToken().withSubject(dto.getSubject()));
        URI form = uriInfo.getBaseUriBuilder().path(ConsentsResource.class).path(dto.getId()).path("submit").queryParam("t", token).build();
        dto.setFormURI(form);
        return dto;
    }

    /*
    @GET
    @Produces(MediaType.TEXT_HTML)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "form has been generated", content = @Content(example = "consent form HTML code")),
            @APIResponse(responseCode = "401", description = "thin token is either invalid or missing")
    })
    @Operation(summary = "Generate a consent form using a given token")
    public TemplateModel getConsentFormHtml(@QueryParam("t") @NotNull String token, @HeaderParam( "Accept-Language" ) String acceptLanguage) throws TemplateServiceException {
        LOGGER.log(Level.INFO, "GET /consents/form (html)");
        try {
            ConsentForm form = consentService.generateForm(token);
            return templateService.buildModel(form);
        } catch (GenerateFormException | TokenExpiredException | InvalidTokenException | ConsentServiceException e) {
            ConsentFormError error = new ConsentFormError(e);
            error.setLanguage(getLanguage(acceptLanguage));
            return templateService.buildModel(error);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    @Operation(summary = "Submit consent form values")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "consent values has been recorded", content = @Content(example = "consent acknowledgment")),
            @APIResponse(responseCode = "401", description = "token is either invalid or missing")
    })
    public TemplateModel submitConsentValues(MultivaluedMap<String, String> values, @Context UriInfo uriInfo, @HeaderParam( "Accept-Language" ) String acceptLanguage) throws TemplateServiceException {
        LOGGER.log(Level.INFO, "POST /consents/values (html)");
        try {
            if (!values.containsKey("token")) {
                throw new AccessDeniedException("unable to find token in values");
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
    public ConsentFormResult postConsentValuesJson(MultivaluedMap<String, String> values, @Context UriInfo uriInfo) throws AccessDeniedException, TokenExpiredException, InvalidTokenException, ConsentServiceException, TokenServiceException, SubmitConsentException {
        LOGGER.log(Level.INFO, "POST /consents/values (json)");
        if (!values.containsKey("token")) {
            throw new AccessDeniedException("unable to find token in form");
        }
        return this.internalPostConsent(values, uriInfo);
    }

    private ConsentFormResult internalPostConsent(MultivaluedMap<String, String> values, UriInfo uriInfo) throws TokenServiceException, TokenExpiredException, InvalidTokenException, ConsentServiceException, SubmitConsentException {
        ConsentReceipt receipt = consentService.submitConsent(values.get("token").get(0), values);
        UriBuilder uri = uriInfo.getBaseUriBuilder().path(ReceiptsResource.class).path(receipt.getTransaction()).queryParam("t", tokenService.generateToken(new ReceiptContext().setSubject(receipt.getSubject())));
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

     */
}
