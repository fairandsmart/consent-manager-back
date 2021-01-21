package com.fairandsmart.consent.api.resource;

/*-
 * #%L
 * Right Consents Community Edition
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * You accept that the Program was not created with a view to satisfy Your
 * individual requirements. Therefore, you must ensure that the Program
 * comply with Your needs, requirements and constraints. FAIR AND SMART
 * represents and warrants that it holds, without any restriction or
 * reservation, all the legal titles, authorizations and intellectual
 * property rights granted in the context of the GPLv3 License. See the
 * Additional Terms for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <https://www.gnu.org/licenses/>.
 * 
 * You should have received a copy of the Additional Terms along with this
 * program. If not, see <https://www.fairandsmart.com/opensource/>.
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
