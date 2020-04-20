package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.api.dto.CreateModelEntryDto;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.manager.ModelDataSerializationException;
import com.fairandsmart.consent.manager.entity.ModelEntry;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.filter.ModelEntryFilter;
import com.fairandsmart.consent.security.AuthenticationService;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;
import com.fairandsmart.consent.token.TokenService;
import com.fairandsmart.consent.token.TokenServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/consents")
public class ConsentsResource {

    private static final Logger LOGGER = Logger.getLogger(ConsentsResource.class.getName());

    @Inject
    AuthenticationService authenticationService;

    @Inject
    ConsentService consentService;

    @Inject
    TokenService tokenService;

    @Inject
    Template horizontal;

    @Inject
    Template vertical;

    @Inject
    Template receipt;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getForm(@HeaderParam("TOKEN") String token) throws TokenServiceException, InvalidTokenException, TokenExpiredException, EntityNotFoundException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "Getting consent form");
        ConsentContext ctx = tokenService.readToken(token);
        HashMap<String, Object> data = new HashMap<>();

        ModelVersion header = consentService.findActiveModelVersionForKey(ctx.getHeaderKey());
        data.put("header", header);
        data.put("headerContent", header.getData(header.defaultLocale));

        switch (ctx.getOrientation()) {
            case HORIZONTAL:
                return horizontal.data(data);
            default:
                return vertical.data(data);
        }
    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance postConsent() {
        LOGGER.log(Level.INFO, "Posting consent");
        return receipt.data("name", "bob");
    }

    @POST
    @Path("/token")
    @RolesAllowed("admin")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String generateToken(@Context SecurityContext sec, ConsentContext ctx) {
        LOGGER.log(Level.INFO, "POST /consents/token");
        LOGGER.log(Level.FINE, "Authenticated user : " + sec.getUserPrincipal().getName());
        return tokenService.generateToken(ctx);
    }

    @GET
    @Path("/models")
    @Produces(MediaType.APPLICATION_JSON)
    public CollectionPage<ModelEntry> listModelEntries(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("25") int size,
            @QueryParam("type") String type) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /consents/models");
        ModelEntryFilter filter = new ModelEntryFilter();
        filter.setPage(page);
        filter.setSize(size);
        filter.setOwner(authenticationService.getConnectedIdentifier());
        filter.setType(ModelEntry.Type.valueOf(type));
        return consentService.listModelEntries(filter);
    }

    @POST
    @Path("/models")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createModelEntry(@Valid CreateModelEntryDto dto, @Context UriInfo uriInfo) throws ConsentManagerException, EntityNotFoundException, EntityAlreadyExistsException {
        LOGGER.log(Level.INFO, "POST /consents/models");
        String id = consentService.createModelEntry(dto.getType(), dto.getKey(), dto.getName(), dto.getDescription(), dto.getLocale(), dto.getContent());
        URI uri = uriInfo.getRequestUriBuilder().path(id).build();
        ModelEntry entry = consentService.getModelEntry(id);
        return Response.created(uri).entity(entry).build();
    }


}
