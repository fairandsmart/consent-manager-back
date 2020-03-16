package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.api.dto.Page;
import com.fairandsmart.consent.manager.entity.Information;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;

@Path("/api/v2/{oid}/dispositions")
public class DispositionsResource {

    private static final Logger LOGGER = Logger.getLogger(DispositionsResource.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Page<Information> listDispositions(
            @PathParam("oid") String oid,
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("25") int size) {
        PanacheQuery<Information> query = Information.findAll();
        query.page(io.quarkus.panache.common.Page.of(page, size));
        Page<Information> result = new Page<>();
        result.setPageSize(size);
        result.setPage(page);
        result.setTotalPages(query.pageCount());
        result.setTotalCount(Information.count());
        result.setValues(query.list());
        return result;
    }

}
