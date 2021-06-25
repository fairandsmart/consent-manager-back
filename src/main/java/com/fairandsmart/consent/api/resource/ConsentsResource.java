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
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.common.exception.UnexpectedException;
import com.fairandsmart.consent.manager.*;
import com.fairandsmart.consent.manager.entity.Transaction;
import com.fairandsmart.consent.manager.exception.ConsentContextSerializationException;
import com.fairandsmart.consent.manager.exception.GenerateFormException;
import com.fairandsmart.consent.manager.exception.SubmitConsentException;
import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateService;
import com.fairandsmart.consent.token.AccessToken;
import com.fairandsmart.consent.token.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Locale;
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
    @Transactional
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    public Response createTransactionJson(@Valid ConsentContext ctx, @Context UriInfo uriInfo) throws AccessDeniedException, ConsentContextSerializationException {
        LOGGER.log(Level.INFO, "POST /consents (json)");
        Transaction tx = consentService.createTransaction(ctx);
        String token = tokenService.generateToken(new AccessToken().withSubject(tx.id));
        URI uri = uriInfo.getBaseUriBuilder().path(ConsentsResource.class).path(tx.id).queryParam("t", token).build();
        return Response.seeOther(uri).build();
    }

    @GET
    @Path("{txid}")
    @Produces(MediaType.APPLICATION_JSON)
    public TransactionDto getTransaction(@PathParam("txid") String txid, @Context UriInfo uriInfo) throws AccessDeniedException, EntityNotFoundException, ConsentContextSerializationException {
        LOGGER.log(Level.INFO, "GET /consents/" + txid + " (json)");
        Transaction tx = consentService.getTransaction(txid);
        TransactionDto dto = TransactionDto.fromTransaction(tx);
        String token = tokenService.generateToken(new AccessToken().withSubject(txid));
        if (tx.state.equals(Transaction.State.NEW)) {
            dto.setActionURI(uriInfo.getBaseUriBuilder().path(ConsentsResource.class).path(dto.getId()).path("submit").queryParam("t", token).build());
        }
        if (tx.state.equals(Transaction.State.SUBMITTED)) {
            dto.setActionURI(uriInfo.getBaseUriBuilder().path(ConsentsResource.class).path(dto.getId()).path("confirm").queryParam("t", token).build());
        }
        return dto;
    }

    @GET
    @Path("{txid}/submit")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "generated form representation", content = @Content(example = "consent form JSON representation")),
            @APIResponse(responseCode = "401", description = "token is either invalid or missing")
    })
    @Operation(summary = "Generate the consent submit form for the given transaction")
    public ConsentSubmitForm getConsentFormJson(@PathParam("txid") String txid, @HeaderParam( "Accept-Language" ) String acceptLanguage) throws UnexpectedException, AccessDeniedException, EntityNotFoundException, GenerateFormException {
        LOGGER.log(Level.INFO, "GET /consents/" + txid + "/submit (json)");
        return consentService.getConsentForm(txid);
    }

    @GET
    @Path("{txid}/submit")
    @Produces(MediaType.TEXT_HTML)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "generated form representation", content = @Content(example = "consent form HTML representation")),
            @APIResponse(responseCode = "401", description = "token is either invalid or missing")
    })
    @Operation(summary = "Generate the consent submit form for the given transaction")
    public TemplateModel getConsentFormHtml(@PathParam("txid") String txid, @HeaderParam( "Accept-Language" ) String acceptLanguage) throws UnexpectedException {
        LOGGER.log(Level.INFO, "GET /consents/" + txid + "/submit (html)");
        try {
            ConsentSubmitForm form = consentService.getConsentForm(txid);
            return templateService.buildModel(form);
        } catch (GenerateFormException | UnexpectedException | AccessDeniedException | EntityNotFoundException e) {
            ConsentFormError error = new ConsentFormError(e);
            error.setLanguage(getLanguage(acceptLanguage));
            return templateService.buildModel(error);
        }
    }

    @POST
    @Transactional
    @Path("{txid}/submit")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postConsentValuesJson(@PathParam("txid") String txid, MultivaluedMap<String, String> values, @Context UriInfo uriInfo) throws UnexpectedException, AccessDeniedException, SubmitConsentException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "POST /consents/" + txid + "/submit (json)");
        consentService.submitConsentValues(txid, values);
        String token = tokenService.generateToken(new AccessToken().withSubject(txid));
        URI uri = uriInfo.getBaseUriBuilder().path(ConsentsResource.class).path(txid).queryParam("t", token).build();
        return Response.seeOther(uri).build();
    }

    @GET
    @Path("{txid}/confirm")
    @Produces(MediaType.TEXT_HTML)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "generated form representation", content = @Content(example = "confirm form HTML representation")),
            @APIResponse(responseCode = "401", description = "token is either invalid or missing")
    })
    @Operation(summary = "Generate the consent submit form for the given transaction")
    public TemplateModel getConfirmationFormHtml(@PathParam("txid") String txid, @HeaderParam( "Accept-Language" ) String acceptLanguage) throws UnexpectedException {
        LOGGER.log(Level.INFO, "GET /consents/" + txid + "/confirm (html)");
        try {
            ConsentConfirmForm form = consentService.getConfirmationForm(txid);
            return templateService.buildModel(form);
        } catch (GenerateFormException | UnexpectedException | AccessDeniedException | EntityNotFoundException e) {
            ConsentFormError error = new ConsentFormError(e);
            error.setLanguage(getLanguage(acceptLanguage));
            return templateService.buildModel(error);
        }
    }

    @POST
    @Transactional
    @Path("{txid}/confirm")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postConfirmValuesJson(@PathParam("txid") String txid, MultivaluedMap<String, String> values, @Context UriInfo uriInfo) throws UnexpectedException, AccessDeniedException, SubmitConsentException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "GET /consents/" + txid + "/confirm (json)");
        consentService.submitConsentValues(txid, values);
        String token = tokenService.generateToken(new AccessToken().withSubject(txid));
        URI uri = uriInfo.getBaseUriBuilder().path(ConsentsResource.class).path(txid).queryParam("t", token).build();
        return Response.seeOther(uri).build();
    }

    /*
    @POST
    @Path("{txid}/submit")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    @Operation(summary = "Submit consent form values")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "consent values has been recorded", content = @Content(example = "consent acknowledgment")),
            @APIResponse(responseCode = "401", description = "token is either invalid or missing")
    })
    public TemplateModel submitConsentValues(@PathParam("txid") String txid, MultivaluedMap<String, String> values, @Context UriInfo uriInfo, @HeaderParam( "Accept-Language" ) String acceptLanguage) throws UnexpectedException {
        LOGGER.log(Level.INFO, "POST /consents/values (html)");
        try {
            ConsentFormResult result = this.internalPostConsent(txid, values, uriInfo);
            return templateService.buildModel(result);
        } catch (InvalidTokenException | AccessDeniedException | SubmitConsentException | TokenExpiredException | ConsentContextSerializationException | EntityNotFoundException e) {
            ConsentFormError error = new ConsentFormError(e);
            error.setLanguage(getLanguage(acceptLanguage));
            return templateService.buildModel(error);
        }
    }

    /*
    private ConsentFormResult internalPostConsent(String txid, MultivaluedMap<String, String> values, UriInfo uriInfo) throws UnexpectedException, InvalidTokenException, AccessDeniedException, SubmitConsentException, TokenExpiredException, ConsentContextSerializationException, EntityNotFoundException {
        ConsentReceipt receipt = consentService.submitConsentValues(txid, values);
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
     */

    private String getLanguage(String acceptLanguage) {
        LOGGER.log(Level.FINEST, "extracting language from header Accept-Language: " + acceptLanguage);
        return (StringUtils.isNotEmpty(acceptLanguage))?new Locale(acceptLanguage).getLanguage():config.language();
    }
}
