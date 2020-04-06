package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.api.dto.CreateTreatmentDto;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.manager.entity.Treatment;
import com.fairandsmart.consent.manager.filter.TreatmentFilter;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/models/treatments")
public class TreatmentsResource {

    private static final Logger LOGGER = Logger.getLogger(TreatmentsResource.class.getName());

    @Inject
    ConsentService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CollectionPage<Treatment> listFooters(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("25") int size) {
        LOGGER.log(Level.INFO, "GET /models/treatments");
        TreatmentFilter filter = new TreatmentFilter();
        filter.setPage(page);
        filter.setSize(size);
        return service.listTreatments(filter);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTreatment(@Valid CreateTreatmentDto dto, @Context UriInfo uriInfo) throws ConsentManagerException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "POST /models/treatments");
        String id = service.createTreatment(dto.getKey(), dto.getName(), dto.getDescription(), dto.getDefaultLanguage(), dto.getCountry());
        URI uri = uriInfo.getRequestUriBuilder().path(id).build();
        Treatment treatment = service.getTreatment(id);
        return Response.created(uri).entity(treatment).build();
    }

}
