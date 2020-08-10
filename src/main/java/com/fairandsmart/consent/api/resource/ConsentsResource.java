package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.manager.model.UserRecord;
import com.fairandsmart.consent.api.dto.OperatorRecordDto;
import com.fairandsmart.consent.api.template.TemplateModel;
import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.common.validation.SortDirection;
import com.fairandsmart.consent.manager.*;
import com.fairandsmart.consent.manager.entity.Record;
import com.fairandsmart.consent.manager.filter.RecordFilter;
import com.fairandsmart.consent.manager.filter.UserRecordFilter;
import com.fairandsmart.consent.manager.model.Receipt;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/consents")
public class ConsentsResource {

    private static final Logger LOGGER = Logger.getLogger(ConsentsResource.class.getName());

    @Inject
    ConsentService consentService;

    @POST
    @Path("/token")
    @RolesAllowed("admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String generateToken(@Context SecurityContext sec, ConsentContext ctx) {
        LOGGER.log(Level.INFO, "POST /consents/token");
        return consentService.buildToken(ctx);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateModel<ConsentForm> getForm(@HeaderParam("TOKEN") String htoken, @QueryParam("t") String qtoken, @QueryParam("subject") String subject)
            throws AccessDeniedException, TokenExpiredException, EntityNotFoundException, ConsentServiceException, InvalidTokenException {
        LOGGER.log(Level.INFO, "GET /consents");

        String token;
        if (!StringUtils.isEmpty(htoken)) {
            token = htoken;
        } else if (!StringUtils.isEmpty(qtoken)) {
            token = qtoken;
        } else {
            throw new AccessDeniedException("Unable to find token neither in header nor as query param");
        }

        ConsentForm form = consentService.generateForm(token, subject);
        return getConsentFormTemplateModel(form);
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public TemplateModel<Receipt> postConsent(MultivaluedMap<String, String> values)
            throws AccessDeniedException, TokenExpiredException, InvalidTokenException, InvalidConsentException, ConsentServiceException {
        LOGGER.log(Level.INFO, "POST /consents");

        if (!values.containsKey("token")) {
            throw new AccessDeniedException("unable to find token in form");
        }
        Receipt receipt = consentService.submitConsent(values.get("token").get(0), values);

        return getReceiptTemplateModel(receipt);
    }

    @GET
    @Path("/themes/preview")
    @Produces(MediaType.TEXT_HTML)
    public TemplateModel<ConsentForm> getThemePreview(
            @QueryParam("locale") @DefaultValue("en") String locale,
            @QueryParam("orientation") String orientation) throws ModelDataSerializationException {
        LOGGER.log(Level.INFO, "GET /themes/preview");

        ConsentForm.Orientation realOrientation = ConsentForm.Orientation.VERTICAL;
        if (ConsentForm.Orientation.HORIZONTAL.name().equals(orientation)) {
            realOrientation = ConsentForm.Orientation.HORIZONTAL;
        }
        ConsentForm form = consentService.generateLipsumForm(realOrientation, locale);

        return getConsentFormTemplateModel(form);
    }

    @GET
    @Path("/records")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public CollectionPage<Record> listRecords(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("25") int size,
            @QueryParam("query") @DefaultValue("") String query,
            @QueryParam("order") @DefaultValue("id") String order,
            @QueryParam("direction") @Valid @SortDirection @DefaultValue("asc") String direction) {
        LOGGER.log(Level.INFO, "GET /records");
        RecordFilter filter = new RecordFilter();
        filter.setPage(page);
        filter.setSize(size);
        filter.setQuery(URLDecoder.decode(query, StandardCharsets.UTF_8));
        filter.setOrder(order);
        filter.setDirection(direction);
        return consentService.listRecords(filter);
    }

    @GET
    @Path("/records/user")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public CollectionPage<UserRecord> listUserRecords(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("25") int size,
            @QueryParam("user") @DefaultValue("") String user,
            @QueryParam("order") @DefaultValue("id") String order,
            @QueryParam("collectionMethod") String collectionMethod,
            @QueryParam("dateAfter") long dateAfter,
            @QueryParam("dateBefore") long dateBefore,
            @QueryParam("value") String value,
            @QueryParam("direction") @Valid @SortDirection @DefaultValue("asc") String direction) {
        LOGGER.log(Level.INFO, "GET /records/user");
        UserRecordFilter filter = new UserRecordFilter();
        filter.setPage(page);
        filter.setSize(size);
        filter.setUser(URLDecoder.decode(user, StandardCharsets.UTF_8));
        filter.setOrder(order);
        filter.setCollectionMethod(collectionMethod);
        filter.setValue(value);
        filter.setDateAfter(dateAfter);
        filter.setDateBefore(dateBefore);
        filter.setDirection(direction);
        return consentService.listUserRecords(filter);
    }

    @POST
    @Path("/records/user")
    @RolesAllowed("admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_HTML)
    public TemplateModel<Receipt> createOperatorRecords(OperatorRecordDto dto)
            throws AccessDeniedException, InvalidTokenException, InvalidConsentException, TokenExpiredException, ConsentServiceException {
        LOGGER.log(Level.INFO, "POST /records/user");

        if (StringUtils.isEmpty(dto.getToken())) {
            throw new AccessDeniedException("unable to find token in form");
        }
        Receipt receipt = consentService.createOperatorRecords(dto.getToken(), dto.getValues(), dto.getComment());

        return getReceiptTemplateModel(receipt);
    }

    private TemplateModel<ConsentForm> getConsentFormTemplateModel(ConsentForm form) {
        TemplateModel<ConsentForm> model = new TemplateModel<>();
        model.setLocale(LocaleUtils.toLocale(form.getLocale()));
        ResourceBundle bundle = ResourceBundle.getBundle("templates/bundles/consent", model.getLocale());
        model.setBundle(bundle);
        model.setData(form);

        if (form.isConditions()) {
            model.setTemplate("conditions.ftl");
        } else if (form.getOrientation().equals(ConsentForm.Orientation.HORIZONTAL)) {
            model.setTemplate("form-horizontal.ftl");
        } else {
            model.setTemplate("form-vertical.ftl");
        }
        LOGGER.log(Level.FINE, model.toString());
        return model;
    }

    private TemplateModel<Receipt> getReceiptTemplateModel(Receipt receipt) {
        TemplateModel<Receipt> model = new TemplateModel<>();
        model.setLocale(LocaleUtils.toLocale(receipt.getLocale()));
        ResourceBundle bundle = ResourceBundle.getBundle("templates/bundles/consent", model.getLocale());
        model.setBundle(bundle);
        if (!StringUtils.isEmpty(receipt.getTransaction())) {
            model.setData(receipt);
            model.setTemplate("receipt.ftl");
        } else {
            model.setTemplate("no-receipt.ftl");
        }
        LOGGER.log(Level.INFO, model.toString());
        return model;
    }

}
