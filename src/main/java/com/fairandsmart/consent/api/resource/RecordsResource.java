package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.api.dto.RecordDto;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.validation.SortDirection;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.manager.entity.Record;
import com.fairandsmart.consent.manager.filter.RecordFilter;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/records")
public class RecordsResource {

    private static final Logger LOGGER = Logger.getLogger(RecordsResource.class.getName());

    @Inject
    ConsentService consentService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CollectionPage<RecordDto> listRecords(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("25") int size,
            @QueryParam("subject") @DefaultValue("") String subject,
            @QueryParam("elements") List<String> elements,
            @QueryParam("before") @DefaultValue("-1") long before,
            @QueryParam("after") @DefaultValue("-1") long after,
            @QueryParam("order") @DefaultValue("bodyKey") String order,
            @QueryParam("direction") @Valid @SortDirection @DefaultValue("asc") String direction) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /records");
        RecordFilter filter = new RecordFilter();
        filter.setPage(page);
        filter.setSize(size);
        filter.setSubject(subject);
        filter.setStatus(Collections.singletonList(Record.Status.COMMITTED));
        filter.setElements(elements);
        filter.setOrder(order);
        filter.setDirection(direction);
        CollectionPage<Record> records = consentService.listRecords(filter);
        CollectionPage<RecordDto> dto = new CollectionPage<>(records);
        dto.setValues(records.getValues().stream().map(RecordDto::fromRecord).collect(Collectors.toList()));
        return dto;
    }

}
