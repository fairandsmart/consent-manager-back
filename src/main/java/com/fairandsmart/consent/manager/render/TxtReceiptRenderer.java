package com.fairandsmart.consent.manager.render;

import com.fairandsmart.consent.manager.model.Receipt;

import javax.annotation.PostConstruct;
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
    public byte[] render(Receipt receipt) throws RenderingException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
             InputStream style = this.getClass().getClassLoader().getResourceAsStream(STYLESHEET)) {
            Transformer transformer = FACTORY.newTransformer(new StreamSource(style));
            StreamSource xml = new StreamSource(new ByteArrayInputStream(receipt.toXmlBytes()));
            transformer.transform(xml, new StreamResult(output));
            return output.toByteArray();
        } catch ( Exception e ) {
            throw new RenderingException("Unable to render receipt in " + MediaType.TEXT_PLAIN, e);
        }
    }
}