package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.manager.entity.Information;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/models/informations")
public class InformationsResource {

    private static final Logger LOGGER = Logger.getLogger(InformationsResource.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CollectionPage<Information> listInformations(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("25") int size) {
        LOGGER.log(Level.INFO, "GET /models/informations");
        PanacheQuery<Information> query = Information.find("type", Information.Type.HEADER);
        query.page(Page.of(page, size));
        CollectionPage<Information> result = new CollectionPage<>();
        result.setPageSize(size);
        result.setPage(page);
        result.setTotalPages(query.pageCount());
        result.setTotalCount(Information.count());
        result.setValues(query.list());
        return result;
    }

    /*
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public LegalInformation createHeader() {
        LOGGER.log(Level.INFO, "POST /headers");
    }
    */

}
