package com.fairandsmart.consent.manager.render;

import com.fairandsmart.consent.manager.model.Receipt;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.swing.text.Style;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class HtmlReceiptRenderer implements ReceiptRenderer {

    private static final TransformerFactory FACTORY = TransformerFactory.newInstance();
    private static final String STYLESHEET = "xsl/receipt.html.xsl";

    @Override
    public String format() {
        return MediaType.TEXT_HTML;
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
            throw new RenderingException("Unable to render receipt in " + MediaType.TEXT_HTML, e);
        }
    }
}