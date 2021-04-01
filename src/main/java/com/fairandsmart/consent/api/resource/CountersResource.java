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

import com.fairandsmart.consent.api.dto.CounterDto;
import com.fairandsmart.consent.manager.ConsentService;
import io.smallrye.common.constraint.NotNull;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/counters")
@Tag(name = "Counters", description = "Counters metrics")
public class CountersResource {

    private static final Logger LOGGER = Logger.getLogger(CountersResource.class.getName());

    @Inject
    ConsentService consentService;

    @GET
    @Path("/transaction")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", description = "Transaction counter")
    @Operation(summary = "Get the Transaction count between timestamp interval")
    public CounterDto getTransactionCounter(
            @Parameter(description = "timestamp when to start counting") @QueryParam("from") @NotNull long from,
            @Parameter(description = "timestamp when to stop counting") @QueryParam("to") @NotNull long to
            ) {
        LOGGER.log(Level.INFO, "GET /counters/transaction");
        long value = consentService.countTransactionsCreatedBetween(from, to);
        CounterDto dto = new CounterDto().withFromTimestamp(from).withToTimestamp(to).withValue(value);
        return dto;
    }

}
