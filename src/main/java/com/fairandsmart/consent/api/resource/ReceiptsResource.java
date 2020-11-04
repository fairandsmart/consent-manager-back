package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.manager.render.ReceiptRendererNotFoundException;
import com.fairandsmart.consent.manager.render.RenderingException;
import com.fairandsmart.consent.manager.store.ReceiptNotFoundException;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;
import com.fairandsmart.consent.token.TokenServiceException;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/receipts")
public class ReceiptsResource {

    private static final Logger LOGGER = Logger.getLogger(ReceiptsResource.class.getName());

    @Inject
    ConsentService consentService;

    @GET
    @Path("{tid}")
    public Response getReceipt(@PathParam("tid") String transaction, @QueryParam("t") String token, @QueryParam("format") String format) throws ConsentManagerException, ReceiptNotFoundException, TokenServiceException, TokenExpiredException, InvalidTokenException, ReceiptRendererNotFoundException, RenderingException {
        LOGGER.log(Level.INFO, "GET /receipts/" + transaction);
        String mimeType = format != null ? format : "text/plain";
        byte[] receipt = consentService.renderReceipt(token, transaction, mimeType);
        return Response.ok(receipt, mimeType).build();
    }

}
