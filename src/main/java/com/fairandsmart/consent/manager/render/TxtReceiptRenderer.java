package com.fairandsmart.consent.manager.render;

/*-
 * #%L
 * Right Consent / A Consent Manager Platform
 *
 * Authors:
 *
 * Xavier Lefevre <xavier.lefevre@fairandsmart.com> / FairAndSmart
 * Nicolas Rueff <nicolas.rueff@fairandsmart.com> / FairAndSmart
 * Jérôme Blanchard <jerome.blanchard@fairandsmart.com> / FairAndSmart
 * Alan Balbo <alan.balbo@fairandsmart.com> / FairAndSmart
 * Frederic Pierre <frederic.pierre@fairansmart.com> / FairAndSmart
 * Victor Guillaume <victor.guillaume@fairandsmart.com> / FairAndSmart
 * Manon Stremplewski <manon.stremplewski@fairandsmart.com> / FairAndSmart
 * Pauline Kullmann <pauline.kullmmann@fairandsmart.com> / FairAndSmart
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.fairandsmart.consent.manager.model.Receipt;
import com.fairandsmart.consent.manager.model.ThemeInfo;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@ApplicationScoped
public class TxtReceiptRenderer implements ReceiptRenderer {

    private static final TransformerFactory FACTORY = TransformerFactory.newInstance();
    private static final String STYLESHEET = "xsl/receipt.txt.xsl";

    @Override
    public String format() {
        return MediaType.TEXT_PLAIN;
    }

    @Override
    public byte[] render(Receipt receipt, ThemeInfo themeInfo) throws RenderingException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
             InputStream style = this.getClass().getClassLoader().getResourceAsStream(STYLESHEET)) {
            Transformer transformer = FACTORY.newTransformer(new StreamSource(style));
            StreamSource xml = new StreamSource(new ByteArrayInputStream(receipt.toXmlBytes()));
            transformer.transform(xml, new StreamResult(output));
            return output.toByteArray();
        } catch (Exception e) {
            throw new RenderingException("Unable to render receipt in " + MediaType.TEXT_PLAIN, e);
        }
    }
}
