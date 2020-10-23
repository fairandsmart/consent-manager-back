package com.fairandsmart.consent.api.resource;

import com.fairandsmart.consent.common.exception.ConsentManagerException;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.manager.model.Receipt;
import com.fairandsmart.consent.manager.store.ReceiptNotFoundException;
import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateService;
import com.fairandsmart.consent.template.TemplateServiceException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBException;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Path("/receipts")
public class ReceiptsResource {

    private static final Logger LOGGER = Logger.getLogger(ReceiptsResource.class.getName());

    @Inject
    ConsentService consentService;

    @Inject
    TemplateService templateService;

    @GET
    @Path("{tid}")
    @Produces(MediaType.TEXT_HTML)
    public TemplateModel<Receipt> getHtmlReceipt(@PathParam("tid") String transaction) throws ConsentManagerException, ReceiptNotFoundException, TemplateServiceException {
        LOGGER.log(Level.INFO, "GET /receipts/" + transaction);
        Receipt receipt = consentService.getReceipt(transaction);
        return templateService.buildModel(receipt);
    }

    @GET
    @Path("{tid}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getXmlReceipt(@PathParam("tid") String transaction) throws ConsentManagerException, ReceiptNotFoundException, JAXBException {
        LOGGER.log(Level.INFO, "GET /receipts/" + transaction);
        Receipt receipt = consentService.getReceipt(transaction);
        return Response.ok(receipt.toXml(), MediaType.APPLICATION_XML_TYPE).build();
    }

    @GET
    @Path("{tid}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getTxtReceipt(@PathParam("tid") String transaction) throws ConsentManagerException, ReceiptNotFoundException {
        LOGGER.log(Level.INFO, "GET /receipts/" + transaction);
        Receipt receipt = consentService.getReceipt(transaction);
        StreamingOutput stream = out -> {
            try {
                TransformerFactory tFactory = TransformerFactory.newInstance();
                LOGGER.log(Level.INFO, "Factory: " + tFactory.toString());
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("xsl/receipt.txt.xsl");
                LOGGER.log(Level.INFO, "Input XSL: " + is.available());
                StreamSource source = new StreamSource(is);
                Transformer transformer = tFactory.newTransformer(source);
                LOGGER.log(Level.INFO, "Transformer: " + transformer.toString());
                ByteArrayInputStream bais = new ByteArrayInputStream(receipt.toXmlBytes());
                LOGGER.log(Level.INFO, "Input XML: " + bais.available());
                StreamSource xml = new StreamSource(bais);
                transformer.transform(xml, new StreamResult(out));
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Something wrong happen when rendering receipt text", e);
            } finally {
                out.close();
            }
        };
        return Response.ok(stream, MediaType.TEXT_PLAIN_TYPE).build();
    }

    @GET
    @Path("{tid}")
    @Produces({"application/pdf"})
    public Response getPdfReceipt(@PathParam("tid") String transaction) throws ConsentManagerException, ReceiptNotFoundException {
        LOGGER.log(Level.INFO, "GET /receipts/" + transaction);
        Receipt receipt = consentService.getReceipt(transaction);
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        StreamingOutput stream = out -> {
            try {
                Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(this.getClass().getClassLoader().getResourceAsStream("xsl/receipt.pdf.xsl")));
                transformer.setParameter("versionParam", "2.0");
                Source src = new StreamSource(new ByteArrayInputStream(receipt.toXmlBytes()));
                Result res = new SAXResult(fop.getDefaultHandler());
                transformer.transform(src, res);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Something wrong happen when rendering receipt pdf", e);
            } finally {
                out.close();
            }
        };
        return Response.ok(stream, "application/pdf").build();
    }

}
