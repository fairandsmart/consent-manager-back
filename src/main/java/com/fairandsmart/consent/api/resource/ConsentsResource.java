package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.api.dto.CreateModelEntryDto;
import com.fairandsmart.consent.api.template.TemplateModel;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.common.exception.EntityAlreadyExistsException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.*;
import com.fairandsmart.consent.manager.entity.ConsentElementEntry;
import com.fairandsmart.consent.manager.filter.ModelEntryFilter;
import com.fairandsmart.consent.manager.model.Receipt;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;
import org.apache.commons.lang3.LocaleUtils;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/consents")
public class ConsentsResource {

    private static final Logger LOGGER = Logger.getLogger(ConsentsResource.class.getName());

    @Inject
    ConsentService consentService;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateModel<ConsentForm> getForm(@HeaderParam("TOKEN") String htoken, @QueryParam("t") String qtoken) throws  InvalidTokenException, TokenExpiredException, EntityNotFoundException, ConsentServiceException {
        LOGGER.log(Level.INFO, "GET /consents");

        String token;
        if ( htoken != null && !htoken.isEmpty() ) {
            token = htoken;
        } else if ( qtoken != null && !qtoken.isEmpty() ) {
            token = qtoken;
        } else {
            throw new InvalidTokenException("Unable to find any token neither in header, neither as query param");
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
    public TemplateModel postConsent(Map<String, String> values) throws TokenExpiredException, InvalidTokenException, InvalidConsentException, ConsentServiceException {
        LOGGER.log(Level.INFO, "POST /consents");

        if ( !values.containsKey("token") ) {
            throw new InvalidTokenException("unable to find token in form");
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
    @Path("/models")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public CollectionPage<ConsentElementEntry> listModelEntries(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("25") int size,
            @QueryParam("type") String type) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "GET /consents/models");
        ModelEntryFilter filter = new ModelEntryFilter();
        filter.setPage(page);
        filter.setSize(size);
        filter.setType(type);
        return consentService.listEntries(filter);
    }

    @POST
    @Path("/models")
    @RolesAllowed("admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createModelEntry(@Valid CreateModelEntryDto dto, @Context UriInfo uriInfo) throws ConsentManagerException, EntityNotFoundException, EntityAlreadyExistsException {
        LOGGER.log(Level.INFO, "POST /consents/models");
        String id = consentService.createEntry(dto.getKey(), dto.getName(), dto.getDescription(), dto.getLocale(), dto.getContent());
        URI uri = uriInfo.getRequestUriBuilder().path(id).build();
        ConsentElementEntry entry = consentService.getEntry(id);
        return Response.created(uri).entity(entry).build();
    }
}
