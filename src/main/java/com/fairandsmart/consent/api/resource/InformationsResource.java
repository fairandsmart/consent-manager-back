package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.api.dto.CreateInformationDto;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.manager.entity.Information;
import com.fairandsmart.consent.manager.filter.InformationFilter;

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

@Path("/models/informations")
public class InformationsResource {

    private static final Logger LOGGER = Logger.getLogger(InformationsResource.class.getName());

    @Inject
    ConsentService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CollectionPage<Information> listInformations(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("25") int size,
            @QueryParam("type") String type) {
        LOGGER.log(Level.INFO, "GET /models/informations");
        InformationFilter filter = new InformationFilter();
        filter.setPage(page);
        filter.setSize(size);
        filter.setType(Information.Type.valueOf(type));
        return service.listInformations(filter);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createInformation(@Valid CreateInformationDto dto, @Context UriInfo uriInfo) throws ConsentManagerException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "POST /models/informations");
        String id = service.createInformation(dto.getType(), dto.getName(), dto.getDescription(), dto.getDefaultLanguage(), dto.getCountry(), dto.getContent());
        URI uri = uriInfo.getRequestUriBuilder().path(id).build();
        Information information = service.getInformation(id);
        return Response.created(uri).entity(information).build();
    }

}
