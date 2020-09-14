package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.validation.SortDirection;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.manager.entity.Record;
import com.fairandsmart.consent.manager.filter.RecordFilter;
import com.fairandsmart.consent.security.AuthenticationService;
import com.fairandsmart.consent.template.TemplateService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/records")
public class RecordsResource {

    private static final Logger LOGGER = Logger.getLogger(RecordsResource.class.getName());

    @Inject
    ConsentService consentService;

    @Inject
    TemplateService templateService;

    @Inject
    AuthenticationService authenticationService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CollectionPage<Record> listRecords(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("25") int size,
            @QueryParam("query") @DefaultValue("") String query,
            @QueryParam("order") @DefaultValue("bodyKey") String order,
            @QueryParam("direction") @Valid @SortDirection @DefaultValue("asc") String direction) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /records");
        //TODO Change this security policy as a customer should be able to list its own records
        //     Security should be enforced in the service itself
        authenticationService.ensureConnectedIdentifierIsAdmin();
        RecordFilter filter = new RecordFilter();
        filter.setPage(page);
        filter.setSize(size);
        filter.setQuery(URLDecoder.decode(query, StandardCharsets.UTF_8));
        filter.setOrder(order);
        filter.setDirection(direction);
        return consentService.listRecords(filter);
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
