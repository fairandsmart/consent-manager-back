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

import com.fairandsmart.consent.common.consts.Placeholders;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.common.validation.ReceiptMediaType;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.manager.ConsentTransaction;
import com.fairandsmart.consent.manager.exception.ModelDataSerializationException;
import com.fairandsmart.consent.manager.render.ReceiptRendererNotFoundException;
import com.fairandsmart.consent.manager.render.RenderingException;
import com.fairandsmart.consent.manager.store.ReceiptNotFoundException;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;
import com.fairandsmart.consent.token.TokenService;
import com.fairandsmart.consent.token.TokenServiceException;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/receipts")
@Tag(name = "Receipts", description = "Operations related to Consent Receipts")
public class ReceiptsResource {

    private static final Logger LOGGER = Logger.getLogger(ReceiptsResource.class.getName());

    @Inject
    ConsentService consentService;

    @Inject
    TokenService tokenService;

    @POST
    @Path("token")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "thin token has been generated", content = @Content(example = "a token")),
    })
    @Operation(summary = "Generate a thin token from a given transaction")
    public String generateReceiptToken(@NotNull @Valid ConsentTransaction transaction) {
        LOGGER.log(Level.INFO, "POST /receipts/token");
        return tokenService.generateToken(transaction);
    }

    @GET
    @Path("{tid}")
    @APIResponses(value = {
            @APIResponse(responseCode = "404", description = "Unable to find the receipt due to un-existing transaction, format renderer or theme"),
            @APIResponse(responseCode = "401", description = AccessDeniedException.ACCESS_TOKEN_ISSUE),
            @APIResponse(responseCode = "200", description = "receipt has been generated")})
    @Operation(summary = "Get a receipt from a thin token")
    public Response getReceipt(
            @Parameter(name = "tid", description = "The receipt's transaction id", example = Placeholders.NIL_UUID) @PathParam("tid") String transaction,
            @Parameter(name = "t", description = "The receipt access token", required = true) @QueryParam("t") @NotEmpty String token,
            @Parameter(name = "format", description = "The desired receipt format", example = MediaType.TEXT_PLAIN, required = true) @QueryParam("format") @NotEmpty @ReceiptMediaType String format,
            @Parameter(name = "theme", description = "The required theme ID") @QueryParam("theme") String theme) throws ConsentManagerException, ReceiptNotFoundException, TokenServiceException, TokenExpiredException, InvalidTokenException, ReceiptRendererNotFoundException, RenderingException, ModelDataSerializationException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "GET /receipts/{0}", transaction);
        String mimeType = format != null ? format : MediaType.TEXT_HTML;
        byte[] receipt = consentService.renderReceipt(token, transaction, mimeType, theme);
        return Response.ok(receipt, mimeType).build();
    }

}
