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

import com.fairandsmart.consent.api.dto.ExtractionConfigDto;
import com.fairandsmart.consent.api.dto.ExtractionResultDto;
import com.fairandsmart.consent.api.dto.RecordDto;
import com.fairandsmart.consent.common.consts.Placeholders;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.manager.entity.Record;
import com.fairandsmart.consent.notification.NotificationService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("records")
@Tag(name = "Records", description = "Operations related to Consent Records")
public class RecordsResource {

    private static final Logger LOGGER = Logger.getLogger(RecordsResource.class.getName());

    @Inject
    ConsentService consentService;

    @Inject
    NotificationService notificationService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "401", description = AccessDeniedException.NO_OPERATOR_ROLE),
            @APIResponse(responseCode = "200", description = "A Map of all subject Records ordered by key")
    })
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "List all subject Records", description = "Records are ordered by element key and chronological order. Records status is evaluated at runtime.")
    public Map<String, List<RecordDto>> listSubjectRecords(
            @Parameter(description = "Subject to query", example = Placeholders.SHELDON) @QueryParam("subject") String subject
    ) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /records");
        Map<String, List<Record>> records = consentService.listSubjectRecords(subject);
        return records.entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey, e -> e.getValue().stream().map(RecordDto::fromRecord).map(
                                dto -> dto.withNotificationReports(
                                        notificationService.listReports(
                                                dto.getTransaction()
                                        )
                                )
                        ).collect(Collectors.toList())
                )
        );
    }

    @POST
    @Path("extraction")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({
            "text/csv",
            MediaType.APPLICATION_JSON
    })
    @APIResponses(value = {
            @APIResponse(responseCode = "401", description = AccessDeniedException.NO_OPERATOR_ROLE),
            @APIResponse(responseCode = "200", description = "A List of all Records that matches the extraction config")
    })
    @SecurityRequirement(name = "access token", scopes = {"profile"})
    @Operation(summary = "Extract records", description = "Extract records according to the specific provided config")
    public List<ExtractionResultDto> extractRecords(
            @Parameter(description = "Subject to query", example = Placeholders.SHELDON) @NotNull @Valid ExtractionConfigDto dto
    ) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "POST /records/extraction");
        return consentService.extractRecords(
                dto.getCondition().getKey(),
                dto.getCondition().getValue(),
                dto.getCondition().isRegexpValue()).entrySet().stream().map(ExtractionResultDto::build).collect(Collectors.toList()
        );
    }

}
