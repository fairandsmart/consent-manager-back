package com.fairandsmart.consent.support;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/consents/v2/support")
@RegisterRestClient(configKey="consent.support.api")
public interface RemoteSupportService {

    @GET
    @Path("/version")
    @Produces(MediaType.TEXT_PLAIN)
    String getAvailableVersion(@HeaderParam("X-Consent-Instance") String key);

}
