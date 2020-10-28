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
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
    @Produces(MediaType.APPLICATION_XML)
    public Response getXmlReceipt(@PathParam("tid") String transaction, @QueryParam("t") String token) throws ConsentManagerException, ReceiptNotFoundException, TokenServiceException, TokenExpiredException, InvalidTokenException, ReceiptRendererNotFoundException, RenderingException {
        LOGGER.log(Level.INFO, "GET /receipts/" + transaction);
        byte[] receipt = consentService.renderReceipt(token, transaction, MediaType.APPLICATION_XML);
        return Response.ok(receipt, MediaType.APPLICATION_XML).build();
    }

    @GET
    @Path("{tid}")
    @Produces(MediaType.TEXT_HTML)
    public Response getHtmlReceipt(@PathParam("tid") String transaction, @QueryParam("t") String token) throws ConsentManagerException, ReceiptNotFoundException, TokenServiceException, TokenExpiredException, InvalidTokenException, ReceiptRendererNotFoundException, RenderingException {
        LOGGER.log(Level.INFO, "GET /receipts/" + transaction);
        byte[] receipt = consentService.renderReceipt(token, transaction, MediaType.TEXT_HTML);
        return Response.ok(receipt, MediaType.TEXT_HTML).build();
    }

    @GET
    @Path("{tid}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getTxtReceipt(@PathParam("tid") String transaction, @QueryParam("t") String token) throws ConsentManagerException, ReceiptNotFoundException, TokenServiceException, TokenExpiredException, InvalidTokenException, ReceiptRendererNotFoundException, RenderingException {
        LOGGER.log(Level.INFO, "GET /receipts/" + transaction);
        byte[] receipt = consentService.renderReceipt(token, transaction, MediaType.TEXT_PLAIN);
        return Response.ok(receipt, MediaType.APPLICATION_XML).build();
    }

    @GET
    @Path("{tid}")
    @Produces("application/pdf")
    public Response getPdfReceipt(@PathParam("tid") String transaction, @QueryParam("t") String token) throws ConsentManagerException, ReceiptNotFoundException, TokenServiceException, TokenExpiredException, InvalidTokenException, ReceiptRendererNotFoundException, RenderingException {
        LOGGER.log(Level.INFO, "GET /receipts/" + transaction);
        byte[] receipt = consentService.renderReceipt(token, transaction, "application/pdf");
        return Response.ok(receipt, "application/pdf").build();
    }

}
