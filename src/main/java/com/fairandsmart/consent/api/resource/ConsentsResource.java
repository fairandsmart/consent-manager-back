package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.api.dto.CreateModelEntryDto;
import com.fairandsmart.consent.api.template.TemplateModel;
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

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.*;
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

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateModel getForm(@HeaderParam("TOKEN") String token) throws TokenServiceException, InvalidTokenException, TokenExpiredException, EntityNotFoundException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "Getting consent form");
        TemplateModel model = new TemplateModel();
        model.setLocale(Locale.getDefault());

        //TODO :
        // 1. Load existing consents for elements of this context (applying models invalidation strategy to generate all possibles consent key)
        // 2. According to the ConsentContext requisite adopt the correct behaviour for display or not the form
        // 3. If form have to be displayed, load all models to populate
        // 4. Generate a new submission token and populate also the

        ConsentContext ctx = tokenService.readToken(token);
        HashMap<String, Object> data = new HashMap<>();
        data.put("header", consentService.findActiveModelVersionForKey(ctx.getHeader()));
        List<ModelVersion> elements = new ArrayList<>();
        for (String key : ctx.getElements()) {
            elements.add(consentService.findActiveModelVersionForKey(key));
        }
        data.put("elements", elements);
        data.put("footer", consentService.findActiveModelVersionForKey(ctx.getFooter()));
        model.setData(data);

        switch (ctx.getOrientation()) {
            case HORIZONTAL:
                model.setTemplate("horizontal.ftl");
            default:
                model.setTemplate("vertical.ftl");
        }

        return model;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_HTML)
    public TemplateModel postConsent(@HeaderParam("TOKEN") String token, Map<String, Integer> values) throws TokenServiceException, TokenExpiredException, InvalidTokenException, EntityNotFoundException, ModelDataSerializationException {
        LOGGER.log(Level.INFO, "Posting consent");
        TemplateModel model = new TemplateModel();
        model.setLocale(Locale.getDefault());

        //TODO
        ConsentContext ctx = tokenService.readToken(token);

        return model;
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
