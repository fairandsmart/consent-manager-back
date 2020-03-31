package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.manager.entity.Treatment;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/models/treatments")
public class TreatmentsResource {

    private static final Logger LOGGER = Logger.getLogger(TreatmentsResource.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CollectionPage<Treatment> listFooters(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("25") int size) {
        LOGGER.log(Level.INFO, "GET /models/treatments");
        PanacheQuery<Treatment> query = Treatment.findAll();
        query.page(Page.of(page, size));
        CollectionPage<Treatment> result = new CollectionPage<>();
        result.setPageSize(size);
        result.setPage(page);
        result.setTotalPages(query.pageCount());
        result.setTotalCount(Treatment.count());
        result.setValues(query.list());
        return result;
    }

}
