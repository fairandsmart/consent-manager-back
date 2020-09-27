package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.api.dto.RecordDto;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.manager.entity.Record;

import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/subjects")
public class SubjectsResource {

    private static final Logger LOGGER = Logger.getLogger(SubjectsResource.class.getName());

    @Inject
    ConsentService consentService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> list(@QueryParam("name") @NotNull @Min(3) String name) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /subjects");
        return consentService.findSubjects(name);
    }

    @GET
    @Path("{subject}/records")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, List<RecordDto>> listCustomerRecords(@PathParam("subject") String subject) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /subjects/" + subject + "/records");
        Map<String, List<Record>> records = consentService.listSubjectRecords(subject);
        return records.entrySet().stream().collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue().stream().map(r -> RecordDto.fromRecord(r)).collect(Collectors.toList())));
    }

}
