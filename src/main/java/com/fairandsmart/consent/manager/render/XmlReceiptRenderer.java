package com.fairandsmart.consent.manager.render;

import com.fairandsmart.consent.manager.model.Receipt;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
public class XmlReceiptRenderer  implements ReceiptRenderer {

    @Override
    public String format() {
        return MediaType.APPLICATION_XML;
    }

    @Override
    public byte[] render(Receipt receipt) throws RenderingException {
        try {
            return receipt.toXmlBytes();
        } catch ( Exception e ) {
            throw new RenderingException("Unable to render receipt as XML", e);
        }
    }
}