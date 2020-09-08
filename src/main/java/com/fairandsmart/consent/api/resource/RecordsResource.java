package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.validation.SortDirection;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.manager.entity.Record;
import com.fairandsmart.consent.manager.filter.RecordFilter;
import com.fairandsmart.consent.security.AuthenticationService;

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


}
