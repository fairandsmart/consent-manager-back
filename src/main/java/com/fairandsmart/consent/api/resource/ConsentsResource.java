package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.manager.entity.Information;
import com.fairandsmart.consent.manager.entity.Treatment;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;
import com.fairandsmart.consent.token.TokenService;
import com.fairandsmart.consent.token.TokenServiceException;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/consents")
public class ConsentsResource {

    private static final Logger LOGGER = Logger.getLogger(InformationsResource.class.getName());

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
    public TemplateInstance getForm(@HeaderParam("TOKEN") String token) throws TokenServiceException, InvalidTokenException, TokenExpiredException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "Getting consent form");
        ConsentContext ctx = tokenService.readToken(token);
        HashMap<String, Object> data = new HashMap<>();

        Information headerInfo = consentService.findInformationByName(ctx.getHeaderKey());
        data.put("headerContent", headerInfo.content.get(headerInfo.defaultLanguage));

        List<Treatment> treatments = new ArrayList<>();
        for (String treatmentKey : ctx.getTreatmentsKeys()) {
            treatments.add(consentService.findTreatmentByName(treatmentKey));
        }
        data.put("treatments", treatments);

        Information footerInfo = consentService.findInformationByName(ctx.getFooterKey());
        data.put("footerContent", footerInfo.content.get(footerInfo.defaultLanguage));

        switch (ctx.getOrientation()) {
            case HORIZONTAL:
                return horizontal.data(data);
            default:
                return vertical.data(data);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance postConsent(@HeaderParam("TOKEN") String token, Map<String, Integer> values) throws TokenServiceException, TokenExpiredException, InvalidTokenException, EntityNotFoundException {
        LOGGER.log(Level.INFO, "Posting consent");
        ConsentContext ctx = tokenService.readToken(token);
        HashMap<String, Object> data = new HashMap<>();

        Information headerInfo = consentService.findInformationByName(ctx.getHeaderKey());
        data.put("headerContent", headerInfo.content.get(headerInfo.defaultLanguage));

        //Fonctionnement adapté aux limitations de Qute
        List<Treatment> treatments = new ArrayList<>();
        List<Integer> treatmentsConsents = new ArrayList<>();
        Treatment treatment;
        for (String treatmentKey : ctx.getTreatmentsKeys()) {
            treatment = consentService.findTreatmentByName(treatmentKey);
            treatments.add(treatment);
            treatmentsConsents.add(values.getOrDefault(treatment.name, 0));
        }
        data.put("treatments", treatments);
        data.put("treatmentsConsents", treatmentsConsents);

        Information footerInfo = consentService.findInformationByName(ctx.getFooterKey());
        data.put("footerContent", footerInfo.content.get(footerInfo.defaultLanguage));

        return receipt.data(data);
    }
}
