package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.common.exception.AccessDeniedException;
import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.*;
import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateService;
import com.fairandsmart.consent.template.TemplateServiceException;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;
import com.fairandsmart.consent.token.TokenService;
import com.fairandsmart.consent.token.TokenServiceException;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/consents")
public class ConsentsResource {

    private static final Logger LOGGER = Logger.getLogger(ConsentsResource.class.getName());

    @Inject
    ConsentService consentService;

    @Inject
    TokenService tokenService;

    @Inject
    TemplateService templateService;

    @POST
    @Path("token")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String generateToken(ConsentContext ctx) throws AccessDeniedException {
        LOGGER.log(Level.INFO, "POST /consents/token");
        return consentService.buildToken(ctx);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateModel<ConsentForm> getForm(@HeaderParam("TOKEN") String htoken, @QueryParam("t") String qtoken) throws AccessDeniedException, TokenExpiredException, EntityNotFoundException, ConsentServiceException, InvalidTokenException, TemplateServiceException {
        LOGGER.log(Level.INFO, "GET /consents");
        String token;
        if (StringUtils.isNotEmpty(htoken)) {
            token = htoken;
        } else if (StringUtils.isNotEmpty(qtoken)) {
            token = qtoken;
        } else {
            throw new AccessDeniedException("Unable to find token neither in header nor as query param");
        }
        ConsentForm form = consentService.generateForm(token);
        return templateService.buildModel(form);
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public TemplateModel<ConsentFormResult> postConsent(MultivaluedMap<String, String> values, @Context UriInfo uriInfo) throws AccessDeniedException, TokenExpiredException, InvalidTokenException, InvalidConsentException, ConsentServiceException, TokenServiceException, TemplateServiceException {
        LOGGER.log(Level.INFO, "POST /consents");
        if (!values.containsKey("token")) {
            throw new AccessDeniedException("unable to find token in form");
        }
        ConsentTransaction tx = consentService.submitConsent(values.get("token").get(0), values);
        UriBuilder uri = uriInfo.getBaseUriBuilder().path(ReceiptsResource.class).path(tx.getTransaction())
                .queryParam("t", tokenService.generateToken(tx));
        ConsentContext ctx = (ConsentContext) tokenService.readToken(values.get("token").get(0));
        ConsentFormResult templateData = new ConsentFormResult();
        templateData.setContext(ctx);
        if (ctx.getReceiptDisplayType() != null && ctx.getReceiptDisplayType() != ConsentContext.ReceiptDisplayType.NONE) {
                uri.queryParam("format", ctx.getReceiptDisplayType());
        }
        templateData.setReceiptURI(uri.build());
        return templateService.buildModel(templateData);
    }

}
