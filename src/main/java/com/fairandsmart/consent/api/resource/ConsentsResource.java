package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.manager.ConsentService;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/consents")
public class ConsentsResource {

    private static final Logger LOGGER = Logger.getLogger(InformationsResource.class.getName());

    @Inject
    ConsentService service;

    @Inject
    Template horizontal;

    @Inject
    Template vertical;

    @Inject
    Template receipt;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getForm() {
        LOGGER.log(Level.INFO, "Getting consent form");
        return horizontal.data("name", "bob");
    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance postConsent() {
        LOGGER.log(Level.INFO, "Posting consent");
        return receipt.data("name", "bob");
    }


}
