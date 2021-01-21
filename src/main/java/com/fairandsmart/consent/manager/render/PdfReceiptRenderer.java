package com.fairandsmart.consent.manager.render;

/*-
 * #%L
 * Right Consents, a Universal Consents & Preferences Management Platform.
 * %%
 * Copyright (C) 2020 - 2021 Fair And Smart
 * %%
 * This file is part of Right Consents Community Edition.
 * 
 * Right Consents Community Edition is published by FAIR AND SMART under the
 * GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 * 
 * For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 * files, or see https://www.fairandsmart.com/opensource/.
 * #L%
 */

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.util.MimeConstants;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class PdfReceiptRenderer extends ReceiptRenderer {

    private static final Logger LOGGER = Logger.getLogger(PdfReceiptRenderer.class.getName());
    private static final TransformerFactory FACTORY = TransformerFactory.newInstance();
    private static final FopFactory FOP_FACTORY = FopFactory.newInstance(new File(".").toURI());
    private static final String STYLESHEET = "xsl/receipt.pdf.xsl";

    @Override
    public String format() {
        return "application/pdf";
    }

    @Override
    public byte[] render(RenderableReceipt receipt) throws RenderingException {
        LOGGER.log(Level.FINE,  "Starting rendering");
        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
            InputStream style = this.getClass().getClassLoader().getResourceAsStream(STYLESHEET)) {
            FOUserAgent foUserAgent = FOP_FACTORY.newFOUserAgent();
            Fop fop = FOP_FACTORY.newFop(MimeConstants.MIME_PDF, foUserAgent, output);
            Transformer transformer = FACTORY.newTransformer(new StreamSource(style));
            transformer.setParameter("versionParam", "2.0");
            JAXBSource source = new JAXBSource(jaxbContext, receipt);
            LOGGER.log(Level.FINE, source.toString());
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(source, res);
            return output.toByteArray();
        } catch (Exception e) {
            throw new RenderingException("Unable to render receipt in application/pdf", e);
        }
    }
}
