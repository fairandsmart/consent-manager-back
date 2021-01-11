package com.fairandsmart.consent.api.resource;

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
import javax.validation.constraints.NotEmpty;
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
    public List<ExtractionResultDto> extractRecords(@NotEmpty @Valid ExtractionConfigDto dto) throws AccessDeniedException {
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
    public List<ExtractionResultDto> extractRecordsCsv(@NotEmpty @Valid ExtractionConfigDto dto) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "POST /records/extraction");
        return consentService.extractRecords(dto.getCondition().getKey(), dto.getCondition().getValue(), dto.getCondition().isRegexpValue()).entrySet().stream().map(ExtractionResultDto::build).collect(Collectors.toList());
    }

}
