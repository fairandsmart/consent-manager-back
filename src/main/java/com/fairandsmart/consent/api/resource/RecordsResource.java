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

import com.fairandsmart.consent.api.dto.ExtractionConfigDto;
import com.fairandsmart.consent.api.dto.ExtractionResultDto;
import com.fairandsmart.consent.api.dto.RecordDto;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.manager.entity.Record;
import com.fairandsmart.consent.notification.NotificationService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
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
    @APIResponses( value = {
            @APIResponse( responseCode = "401", description = "Access denied (you must be either the subject or an operator)"),
            @APIResponse( responseCode = "200", description = "A Map of all subject Records ordered by element key") })
    @Operation( operationId = "listSubjectRecords", summary = "List all subject Records ordered by element key and chronological order. Records status is evaluated at runtime.")
    public Map<String, List<RecordDto>> listSubjectRecords(@QueryParam("subject") String subject) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /records");
        Map<String, List<Record>> records = consentService.listSubjectRecords(subject);
        return records.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, (e) -> e.getValue().stream().map(RecordDto::fromRecord).map(dto -> dto.withNotificationReports(notificationService.listReports(dto.getTransaction()))).collect(Collectors.toList())));
    }

    @POST
    @Path("extraction")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses( value = {
            @APIResponse( responseCode = "401", description = "Access denied (you must be either the subject or an operator)"),
            @APIResponse( responseCode = "200", description = "A List of all Records that matches the extraction config") })
    @Operation( operationId = "extractRecords", summary = "Extract records according to the specific provided config")
    public List<ExtractionResultDto> extractRecords(@NotNull @Valid ExtractionConfigDto dto) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "POST /records/extraction");
        return consentService.extractRecords(dto.getCondition().getKey(), dto.getCondition().getValue(), dto.getCondition().isRegexpValue()).entrySet().stream().map(ExtractionResultDto::build).collect(Collectors.toList());
    }

    @POST
    @Path("extraction")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("text/csv")
    @APIResponses( value = {
            @APIResponse( responseCode = "401", description = "Access denied (you must be either the subject or an operator)"),
            @APIResponse( responseCode = "200", description = "A List of all Records that matches the extraction config") })
    @Operation( operationId = "extractRecords", summary = "Extract records according to the specific provided config")
    public List<ExtractionResultDto> extractRecordsCsv(@NotNull @Valid ExtractionConfigDto dto) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "POST /records/extraction");
        return consentService.extractRecords(dto.getCondition().getKey(), dto.getCondition().getValue(), dto.getCondition().isRegexpValue()).entrySet().stream().map(ExtractionResultDto::build).collect(Collectors.toList());
    }

}
