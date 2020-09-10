package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.*;
import com.fairandsmart.consent.manager.model.Receipt;
import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateService;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/consents")
public class ConsentsResource {

    private static final Logger LOGGER = Logger.getLogger(ConsentsResource.class.getName());

    @Inject
    ConsentService consentService;

    @Inject
    TemplateService templateService;

    @POST
    @Path("/token")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String generateToken(ConsentContext ctx) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "POST /consents/token");
        return consentService.buildToken(ctx);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateModel<ConsentForm> getForm(@HeaderParam("TOKEN") String htoken, @QueryParam("t") String qtoken, @QueryParam("subject") String subject) throws AccessDeniedException, TokenExpiredException, EntityNotFoundException, ConsentServiceException, InvalidTokenException {
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
        return templateService.getFormTemplate(form);
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public TemplateModel<Receipt> postConsent(MultivaluedMap<String, String> values) throws AccessDeniedException, TokenExpiredException, InvalidTokenException, InvalidConsentException, ConsentServiceException {
        LOGGER.log(Level.INFO, "POST /consents");
        if (!values.containsKey("token")) {
            throw new AccessDeniedException("unable to find token in form");
        }
        Receipt receipt = consentService.submitConsent(values.get("token").get(0), values);
        return templateService.getReceiptTemplate(receipt);
    }

}
