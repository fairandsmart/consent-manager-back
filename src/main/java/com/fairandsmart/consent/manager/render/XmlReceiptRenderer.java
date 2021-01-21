package com.fairandsmart.consent.manager.render;

/*-
 * #%L
 * Right Consents Community Edition
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
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class XmlReceiptRenderer extends ReceiptRenderer {

    private static final Logger LOGGER = Logger.getLogger(XmlReceiptRenderer.class.getName());

    @Override
    public String format() {
        return MediaType.APPLICATION_XML;
    }

    @Override
    public byte[] render(RenderableReceipt receipt) throws RenderingException {
        LOGGER.log(Level.FINE,  "Starting rendering");
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            marshaller.marshal(receipt, out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RenderingException("Unable to render receipt as XML", e);
        }
    }
}
