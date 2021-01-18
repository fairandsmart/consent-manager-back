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

import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
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
@Tag(name = "Receipt")
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
    public String generateReceiptToken(@NotNull @Valid ConsentTransaction transaction) {
        LOGGER.log(Level.INFO, "POST /receipts/token");
        return tokenService.generateToken(transaction);
    }

    @GET
    @Path("{tid}")
    @APIResponses( value = {
            @APIResponse( responseCode = "500", description = "Something unexpected occurred" ),
            @APIResponse( responseCode = "404", description = "Unable to find the receipt due to un-existing transaction, format renderer or theme" ),
            @APIResponse( responseCode = "401", description = "Access denied (bad token)"),
            @APIResponse( responseCode = "200", description = "The receipt in the desired format") })
    @Operation( operationId = "getReceipt", summary = "Get a representation of a receipt according to the desired format and theme")
    public Response getReceipt(
            @Parameter(name = "tid", description = "The transaction id of the receipt", example = "1b1a256a-9f5b-4171-a54b-ff762889d1d1")
            @PathParam("tid") String transaction,
            @Parameter(name = "t", description = "The receipt access token", required = true)
            @QueryParam("t") @NotEmpty String token,
            @Parameter(name = "format", description = "The desired receipt format (a valid and supported mimetype)", example = "application/pdf", required = true)
            @QueryParam("format") @NotEmpty String format,
            @Parameter(name = "theme", description = "The desired receipt theme (a valid and active theme key)", required = false)
            @QueryParam("theme") String theme) throws ConsentManagerException, ReceiptNotFoundException, TokenServiceException, TokenExpiredException, InvalidTokenException, ReceiptRendererNotFoundException, RenderingException, ModelDataSerializationException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "GET /receipts/" + transaction);
        String mimeType = format != null ? format : "text/html";
        byte[] receipt = consentService.renderReceipt(token, transaction, mimeType, theme);
        return Response.ok(receipt, mimeType).build();
    }

}
