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

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class HtmlReceiptRenderer extends ReceiptRenderer {

    private static final Logger LOGGER = Logger.getLogger(HtmlReceiptRenderer.class.getName());
    private static final TransformerFactory FACTORY = TransformerFactory.newInstance();
    private static final String STYLESHEET = "xsl/receipt.html.xsl";

    @Override
    public String format() {
        return MediaType.TEXT_HTML;
    }

    @Override
    public byte[] render(RenderableReceipt receipt) throws RenderingException {
        LOGGER.log(Level.FINE,  "Starting rendering");
        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
            InputStream style = this.getClass().getClassLoader().getResourceAsStream(STYLESHEET)) {
            Transformer transformer = FACTORY.newTransformer(new StreamSource(style));
            JAXBSource source = new JAXBSource(jaxbContext, receipt);
            LOGGER.log(Level.FINE,  source.toString());
            transformer.transform(source, new StreamResult(output));
            return output.toByteArray();
        } catch (Exception e) {
            throw new RenderingException("Unable to render receipt in " + MediaType.TEXT_HTML, e);
        }
    }
}
