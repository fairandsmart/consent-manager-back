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

import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.stats.StatisticsService;
import com.fairandsmart.consent.stats.dto.StatsBag;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("stats")
@Tag(name = "Statistics", description = "Operations related to statistics")
public class StatisticsResource {

    private static final Logger LOGGER = Logger.getLogger(StatisticsResource.class.getName());

    @Inject
    StatisticsService statsService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "401", description = AccessDeniedException.NO_ADMIN_ROLE),
            @APIResponse(responseCode = "200", description = "StatBag representation of all available statistics")
    })
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "Get all backend generated statistics")
    public StatsBag getStats() throws AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /stats");
        return statsService.get();
    }

}
