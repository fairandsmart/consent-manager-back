package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.api.dto.RecordDto;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.common.validation.SortDirection;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.manager.entity.Record;
import com.fairandsmart.consent.manager.filter.RecordFilter;
import com.fairandsmart.consent.security.AuthenticationService;

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
            @QueryParam("elements") @DefaultValue("") List<String> elements,
            @QueryParam("before") @DefaultValue("-1") long before,
            @QueryParam("after") @DefaultValue("-1") long after,
            @QueryParam("order") @DefaultValue("bodyKey") String order,
            @QueryParam("direction") @Valid @SortDirection @DefaultValue("asc") String direction) throws AccessDeniedException, EntityNotFoundException {
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
        dto.setValues(records.getValues().stream().map(e -> RecordDto.fromRecord(e)).collect(Collectors.toList()));
        return dto;
    }

    /*
    @GET
    @Path("/subset")
    @Produces(MediaType.APPLICATION_JSON)
    public CollectionPage<UserRecord> listRecordsForUsers(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("25") int size,
            @QueryParam("order") @DefaultValue("bodyKey") String order,
            @QueryParam("direction") @Valid @SortDirection @DefaultValue("asc") String direction,
            @QueryParam("users") List<String> users,
            @QueryParam("treatments") @DefaultValue("") List<String> treatments,
            @QueryParam("conditions") @DefaultValue("") List<String> conditions) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /records/subset");
        authenticationService.ensureConnectedIdentifierIsAdmin();
        MixedRecordsFilter filter = new MixedRecordsFilter();
        filter.setPage(page);
        filter.setSize(size);
        filter.setOrder(order);
        filter.setDirection(direction);
        filter.setUsers(users.stream().map(user -> URLDecoder.decode(user, StandardCharsets.UTF_8)).collect(Collectors.toList()));
        filter.setTreatments(treatments);
        filter.setConditions(conditions);
        return consentService.listRecordsForUsers(filter);
    }

    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public CollectionPage<UserRecord> listUserRecords(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("25") int size,
            @QueryParam("user") @DefaultValue("") String user,
            @QueryParam("order") @DefaultValue("bodyKey") String order,
            @QueryParam("direction") @Valid @SortDirection @DefaultValue("asc") String direction,
            @QueryParam("collectionMethod") String collectionMethod,
            @QueryParam("dateAfter") long dateAfter,
            @QueryParam("dateBefore") long dateBefore,
            @QueryParam("value") String value) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /records/user");
        authenticationService.ensureConnectedIdentifierIsAdmin();
        if ( user.isEmpty() ) {
            throw new BadRequestException("Missing user parameter");
        }
        UserRecordFilter filter = new UserRecordFilter();
        filter.setPage(page);
        filter.setSize(size);
        filter.setUser(URLDecoder.decode(user, StandardCharsets.UTF_8));
        filter.setOrder(order);
        filter.setCollectionMethod(collectionMethod);
        filter.setValue(value);
        filter.setDateAfter(dateAfter);
        filter.setDateBefore(dateBefore);
        filter.setDirection(direction);
        return consentService.listUserRecords(filter);
    }

    @POST
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_HTML)
    public TemplateModel<Receipt> createOperatorRecords(OperatorRecordDto dto)
            throws AccessDeniedException, InvalidTokenException, InvalidConsentException, TokenExpiredException, ConsentServiceException, TemplateServiceException {
        LOGGER.log(Level.INFO, "POST /records/user");
        authenticationService.ensureConnectedIdentifierIsOperator();
        if (StringUtils.isEmpty(dto.getToken())) {
            throw new AccessDeniedException("unable to find token in form");
        }
        Receipt receipt = consentService.createOperatorRecords(dto.getToken(), dto.getValues(), dto.getComment());
        return templateService.buildModel(receipt);
    }
    */

}
