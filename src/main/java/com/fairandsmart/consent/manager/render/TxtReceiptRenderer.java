package com.fairandsmart.consent.manager.render;

/*-
 * #%L
 * Right Consents / A Consent Manager Platform
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * You accept that the Program was not created with a view to satisfy Your
 * individual requirements. Therefore, you must ensure that the Program
 * comply with Your needs, requirements and constraints. FAIR AND SMART
 * represents and warrants that it holds, without any restriction or
 * reservation, all the legal titles, authorizations and intellectual
 * property rights granted in the context of the GPLv3 License. See the
 * Additional Terms for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <https://www.gnu.org/licenses/>.
 * 
 * You should have received a copy of the Additional Terms along with this
 * program. If not, see <https://www.fairandsmart.com/opensource/>.
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
public class TxtReceiptRenderer extends ReceiptRenderer {

    private static final Logger LOGGER = Logger.getLogger(TxtReceiptRenderer.class.getName());
    private static final TransformerFactory FACTORY = TransformerFactory.newInstance();
    private static final String STYLESHEET = "xsl/receipt.txt.xsl";

    @Override
    public String format() {
        return MediaType.TEXT_PLAIN;
    }

    @Override
    public byte[] render(RenderableReceipt receipt) throws RenderingException {
        LOGGER.log(Level.FINE,  "Starting rendering");
        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
            InputStream style = this.getClass().getClassLoader().getResourceAsStream(STYLESHEET)) {
            Transformer transformer = FACTORY.newTransformer(new StreamSource(style));
            JAXBSource source = new JAXBSource(jaxbContext, receipt);
            transformer.transform(source, new StreamResult(output));
            return output.toByteArray();
        } catch (Exception e) {
            throw new RenderingException("Unable to render receipt in " + MediaType.TEXT_PLAIN, e);
        }
    }
}
