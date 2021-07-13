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

import com.fairandsmart.consent.manager.ConsentTransaction;
import com.fairandsmart.consent.common.config.MainConfig;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.common.exception.UnexpectedException;
import com.fairandsmart.consent.manager.*;
import com.fairandsmart.consent.manager.entity.Transaction;
import com.fairandsmart.consent.manager.exception.ConfirmationException;
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
import java.io.IOException;
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
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTransactionJson(@Valid ConsentContext ctx, @Context UriInfo uriInfo, @HeaderParam("Accept-Language") String acceptLanguage) throws AccessDeniedException, ConsentContextSerializationException {
        LOGGER.log(Level.INFO, "POST /consents (json)");
        if (ctx.getLanguage() == null || ctx.getLanguage().isEmpty()) {
            ctx.setLanguage(getLanguage(acceptLanguage));
        }
        Transaction tx = consentService.createTransaction(ctx);
        String token = tokenService.generateToken(new AccessToken().withSubject(tx.id));
        URI uri = uriInfo.getBaseUriBuilder().path(ConsentsResource.class).path(tx.id).queryParam("t", token).build();
        return Response.created(uri).entity(uri.toString()).build();
    }

    @GET
    @Path("{txid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ConsentTransaction getTransactionJson(@PathParam("txid") String txid, @Context UriInfo uriInfo) throws AccessDeniedException, EntityNotFoundException, ConsentContextSerializationException {
        LOGGER.log(Level.INFO, "GET /consents/" + txid + " (json)");
        Transaction tx = consentService.getTransaction(txid);
        ConsentTransaction dto = ConsentTransaction.fromTransaction(tx);
        String token = tokenService.generateToken(new AccessToken().withSubject(txid));
        dto.setToken(token);
        if (tx.state.getTask() != null) {
            dto.setTask(uriInfo.getBaseUriBuilder().path(ConsentsResource.class).path(dto.getId()).path(tx.state.getTask()).build());
        }
        return dto;
    }

    @GET
    @Path("{txid}")
    @Produces(MediaType.TEXT_HTML)
    public Response getTransactionHtml(@PathParam("txid") String txid, @Context UriInfo uriInfo, @HeaderParam("Accept-Language") String acceptLanguage) throws UnexpectedException {
        LOGGER.log(Level.INFO, "GET /consents/" + txid + " (html)");
        try {
            Transaction tx = consentService.getTransaction(txid);
            String token = tokenService.generateToken(new AccessToken().withSubject(txid));
            if (tx.state.getTask() != null) {
                LOGGER.log(Level.FINE, "Transaction task exists, redirecting to task uri");
                return Response.seeOther(uriInfo.getBaseUriBuilder().path(ConsentsResource.class).path(txid).path(tx.state.getTask()).queryParam("t", token).build()).build();
            } else {
                LOGGER.log(Level.FINE, "Transaction task not found, building transaction view");
                ConsentTransaction dto = ConsentTransaction.fromTransaction(tx);
                dto.setToken(token);
                dto.setBreed(uriInfo.getBaseUriBuilder().path(ConsentsResource.class).path(tx.id).path("child").build());
                dto.setReceipt(uriInfo.getBaseUriBuilder().path(ReceiptsResource.class).path(tx.id).build());
                TemplateModel template = templateService.buildModel(dto);
                return Response.ok(template).build();
            }
        } catch (UnexpectedException | AccessDeniedException | EntityNotFoundException | ConsentContextSerializationException e) {
            ConsentFormError error = new ConsentFormError(e);
            error.setLanguage(getLanguage(acceptLanguage));
            return Response.ok(templateService.buildModel(error)).build();
        }
    }

    @GET
    @Path("{txid}/submit")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "submission form representation", content = @Content(example = "submission form JSON representation")),
            @APIResponse(responseCode = "401", description = "token is either invalid or missing")
    })
    @Operation(summary = "Generate the consent submission form for the given transaction")
    public ConsentSubmitForm getSubmissionFormJson(@PathParam("txid") String txid) throws UnexpectedException, AccessDeniedException, EntityNotFoundException, GenerateFormException {
        LOGGER.log(Level.INFO, "GET /consents/" + txid + "/submit (json)");
        return consentService.getConsentForm(txid);
    }

    @GET
    @Path("{txid}/submit")
    @Produces(MediaType.TEXT_HTML)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "submission form representation", content = @Content(example = "submission form HTML representation")),
            @APIResponse(responseCode = "401", description = "token is either invalid or missing")
    })
    @Operation(summary = "Generate the consent submission form for the given transaction")
    public TemplateModel getSubmissionFormHtml(@PathParam("txid") String txid, @HeaderParam("Accept-Language") String acceptLanguage) throws UnexpectedException {
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postSubmissionValuesJson(@PathParam("txid") String txid, MultivaluedMap<String, String> values, @Context UriInfo uriInfo) throws UnexpectedException, AccessDeniedException, SubmitConsentException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "POST /consents/" + txid + "/submit (json)");
        consentService.submitConsentValues(txid, values);
        String token = tokenService.generateToken(new AccessToken().withSubject(txid));
        URI uri = uriInfo.getBaseUriBuilder().path(ConsentsResource.class).path(txid).queryParam("t", token).build();
        return Response.created(uri).build();
    }

    @POST
    @Transactional
    @Path("{txid}/submit")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response postSubmissionValuesHtml(@PathParam("txid") String txid, MultivaluedMap<String, String> values, @Context UriInfo uriInfo) throws UnexpectedException, AccessDeniedException, SubmitConsentException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "POST /consents/" + txid + "/submit (html)");
        consentService.submitConsentValues(txid, values);
        String token = tokenService.generateToken(new AccessToken().withSubject(txid));
        URI uri = uriInfo.getBaseUriBuilder().path(ConsentsResource.class).path(txid).queryParam("t", token).build();
        return Response.seeOther(uri).build();
    }

    @GET
    @Transactional
    @Path("{txid}/confirm")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "confirmation form representation", content = @Content(example = "confirmation form JSON representation")),
            @APIResponse(responseCode = "401", description = "token is either invalid or missing")
    })
    @Operation(summary = "Generate the consent confirmation form for the given transaction")
    public ConsentConfirmForm getConfirmationFormJson(@PathParam("txid") String txid) throws UnexpectedException, AccessDeniedException, EntityNotFoundException, GenerateFormException {
        LOGGER.log(Level.INFO, "GET /consents/" + txid + "/confirm (json)");
        return consentService.getConfirmationForm(txid);
    }

    @GET
    @Transactional
    @Path("{txid}/confirm")
    @Produces(MediaType.TEXT_HTML)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "confirmation form representation", content = @Content(example = "confirmation form HTML representation")),
            @APIResponse(responseCode = "401", description = "token is either invalid or missing")
    })
    @Operation(summary = "Generate the consent confirmation form for the given transaction")
    public TemplateModel getConfirmationFormHtml(@PathParam("txid") String txid, @HeaderParam("Accept-Language") String acceptLanguage) throws UnexpectedException {
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
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postConfirmationValuesJson(@PathParam("txid") String txid, MultivaluedMap<String, String> values, @Context UriInfo uriInfo) throws UnexpectedException, AccessDeniedException, SubmitConsentException, EntityNotFoundException, ConfirmationException {
        LOGGER.log(Level.INFO, "POST /consents/" + txid + "/confirm (json)");
        consentService.submitConfirmationValues(txid, values);
        String token = tokenService.generateToken(new AccessToken().withSubject(txid));
        URI uri = uriInfo.getBaseUriBuilder().path(ConsentsResource.class).path(txid).queryParam("t", token).build();
        return Response.created(uri).build();
    }

    @POST
    @Transactional
    @Path("{txid}/confirm")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response postConfirmationValuesHtml(@PathParam("txid") String txid, MultivaluedMap<String, String> values, @Context UriInfo uriInfo) throws UnexpectedException, AccessDeniedException, SubmitConsentException, EntityNotFoundException, ConfirmationException {
        LOGGER.log(Level.INFO, "POST /consents/" + txid + "/confirm (html)");
        consentService.submitConfirmationValues(txid, values);
        String token = tokenService.generateToken(new AccessToken().withSubject(txid));
        URI uri = uriInfo.getBaseUriBuilder().path(ConsentsResource.class).path(txid).queryParam("t", token).build();
        return Response.seeOther(uri).build();
    }

    @POST
    @Transactional
    @Path("{txid}/child")
    @Produces(MediaType.APPLICATION_JSON)
    public Response breedTransactionJson(@PathParam("txid") String txid, @Context UriInfo uriInfo) throws AccessDeniedException, ConsentContextSerializationException, IOException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "POST /consents/" + txid + "/child (json)");
        Transaction tx = consentService.breedTransaction(txid);
        String token = tokenService.generateToken(new AccessToken().withSubject(tx.id));
        URI uri = uriInfo.getBaseUriBuilder().path(ConsentsResource.class).path(tx.id).queryParam("t", token).build();
        return Response.created(uri).build();
    }

    @GET
    @Transactional
    @Path("{txid}/child")
    @Produces(MediaType.TEXT_HTML)
    public Response breedTransactionHtml(@PathParam("txid") String txid, @Context UriInfo uriInfo) throws AccessDeniedException, ConsentContextSerializationException, IOException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "GET /consents/" + txid + "/child (html)");
        Transaction tx = consentService.breedTransaction(txid);
        String token = tokenService.generateToken(new AccessToken().withSubject(tx.id));
        URI uri = uriInfo.getBaseUriBuilder().path(ConsentsResource.class).path(tx.id).queryParam("t", token).build();
        return Response.seeOther(uri).build();
    }

    private String getLanguage(String acceptLanguage) {
        LOGGER.log(Level.FINEST, "extracting language from header Accept-Language: " + acceptLanguage);
        return (StringUtils.isNotEmpty(acceptLanguage)) ? new Locale(acceptLanguage).getLanguage() : config.language();
    }

    @POST
    @Path("preview")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_HTML)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "submission form representation", content = @Content(example = "submission form HTML representation"))
    })
    @Operation(summary = "Generate the consent submission form for the given transaction")
    public TemplateModel getSubmissionFormPreview(@Valid ConsentContext ctx, @Context UriInfo uriInfo, @HeaderParam("Accept-Language") String acceptLanguage) throws UnexpectedException {
        LOGGER.log(Level.INFO, "GET /consents/preview (html)");
        try {
            ConsentSubmitForm form = consentService.getConsentFormPreview(ctx);
            return templateService.buildModel(form);
        } catch (GenerateFormException | UnexpectedException | AccessDeniedException | EntityNotFoundException e) {
            ConsentFormError error = new ConsentFormError(e);
            error.setLanguage(getLanguage(acceptLanguage));
            return templateService.buildModel(error);
        }
    }
}
