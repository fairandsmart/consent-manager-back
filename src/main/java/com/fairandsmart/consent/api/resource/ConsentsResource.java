package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.api.dto.*;
import com.fairandsmart.consent.api.template.TemplateModel;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.common.validation.UUID;
import com.fairandsmart.consent.manager.*;
import com.fairandsmart.consent.manager.entity.ConsentElementEntry;
import com.fairandsmart.consent.manager.entity.ConsentElementVersion;
import com.fairandsmart.consent.manager.filter.EntryFilter;
import com.fairandsmart.consent.manager.model.*;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;
import org.apache.commons.lang3.LocaleUtils;

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
    ConsentService consentService;

    @GET
    @Path("/entries")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public CollectionPage<ConsentElementEntry> listEntries(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("25") int size,
            @QueryParam("types") List<String> types) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /consents/entries");
        EntryFilter filter = new EntryFilter();
        filter.setPage(page);
        filter.setSize(size);
        filter.setTypes(types);
        return consentService.listEntries(filter);
    }

    @POST
    @Path("/entries")
    @RolesAllowed("admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createEntry(@Valid CreateEntryDto dto, @Context UriInfo uriInfo) throws ConsentManagerException, EntityNotFoundException, EntityAlreadyExistsException {
        LOGGER.log(Level.INFO, "POST /consents/entries");
        String id = consentService.createEntry(dto.getKey(), dto.getName(), dto.getDescription(), dto.getType());
        URI uri = uriInfo.getRequestUriBuilder().path(id).build();
        ConsentElementEntry entry = consentService.getEntry(id);
        return Response.created(uri).entity(entry).build();
    }

    @GET
    @Path("/entries/{id}")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public ConsentElementEntry getEntry(@PathParam("id") @Valid @UUID String id) throws EntityNotFoundException, AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /consents/entries/" + id);
        return consentService.getEntry(id);
    }

    @PUT
    @Path("/entries/{id}")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public ConsentElementEntry updateEntry(@PathParam("id") @Valid @UUID String id, UpdateEntryDto dto) throws EntityNotFoundException, AccessDeniedException {
        LOGGER.log(Level.INFO, "PUT /consents/entries/" + id);
        return consentService.updateEntry(id, dto.getName(), dto.getDescription());
    }

    @PUT
    @Path("/entries/{id}/content")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public ConsentElementVersion updateEntryContent(@PathParam("id") @Valid @UUID String id, UpdateEntryContentDto dto) throws EntityNotFoundException, ConsentManagerException {
        LOGGER.log(Level.INFO, "PUT /consents/entries/" + id + "/content");
        return consentService.updateEntryContent(id, dto.getLocale(), dto.getContent());
    }

    @PUT
    @Path("/entries/{id}/status")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEntryStatus(@PathParam("id") @Valid @UUID String id, UpdateEntryStatusDto dto) throws EntityNotFoundException, ConsentManagerException {
        LOGGER.log(Level.INFO, "PUT /consents/entries/" + id + "/status");
        LOGGER.log(Level.INFO, "dto: " + dto);
        switch (dto.getStatus()) {
            case DRAFT:
                return Response.status(Response.Status.CONFLICT).build();
            case ACTIVE:
                consentService.activateEntry(id, dto.getRevocation());
                return Response.status(Response.Status.NO_CONTENT).build();
            case ARCHIVED:
                consentService.archiveEntry(id, dto.getRevocation());
                return Response.status(Response.Status.NO_CONTENT).build();
            default:
                return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/entries/{id}/versions")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ConsentElementVersion> listEntryVersions(@PathParam("id") @Valid @UUID String id) throws ConsentManagerException {
        LOGGER.log(Level.INFO, "GET /consents/entries/" + id + "/versions");
        return consentService.listVersionsForEntry(id);
    }

    @POST
    @Path("/token")
    @RolesAllowed("admin")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String generateToken(@Context SecurityContext sec, ConsentContext ctx) {
        LOGGER.log(Level.INFO, "POST /consents/token");
        return consentService.buildToken(ctx);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateModel<ConsentForm> getForm(@HeaderParam("TOKEN") String htoken, @QueryParam("t") String qtoken) throws AccessDeniedException, TokenExpiredException, EntityNotFoundException, ConsentServiceException, InvalidTokenException {
        LOGGER.log(Level.INFO, "GET /consents");

        String token;
        if (htoken != null && !htoken.isEmpty()) {
            token = htoken;
        } else if (qtoken != null && !qtoken.isEmpty()) {
            token = qtoken;
        } else {
            throw new AccessDeniedException("Unable to find token neither in header nor as query param");
        }

        ConsentForm form = consentService.generateForm(token);

        TemplateModel<ConsentForm> model = new TemplateModel();
        model.setLocale(LocaleUtils.toLocale(form.getLocale()));
        ResourceBundle bundle = ResourceBundle.getBundle("templates/bundles/consent", model.getLocale());
        model.setBundle(bundle);
        model.setData(form);
        model.setTemplate("form-vertical.ftl");
        if (form.getOrientation().equals(ConsentForm.Orientation.HORIZONTAL)) {
            model.setTemplate("form-horizontal.ftl");
        }
        LOGGER.log(Level.FINE, model.toString());
        return model;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_HTML)
    public TemplateModel postConsent(Map<String, String> values) throws AccessDeniedException, TokenExpiredException, InvalidTokenException, InvalidConsentException, ConsentServiceException {
        LOGGER.log(Level.INFO, "POST /consents");

        if (!values.containsKey("token")) {
            throw new AccessDeniedException("unable to find token in form");
        }
        Receipt receipt = consentService.submitConsent(values.get("token"), values);

        TemplateModel model = new TemplateModel();
        model.setLocale(LocaleUtils.toLocale(receipt.getLocale()));
        ResourceBundle bundle = ResourceBundle.getBundle("templates/bundles/consent", model.getLocale());
        model.setBundle(bundle);
        model.setData(receipt);
        model.setTemplate("receipt.ftl");
        LOGGER.log(Level.INFO, model.toString());
        return model;
    }

}
