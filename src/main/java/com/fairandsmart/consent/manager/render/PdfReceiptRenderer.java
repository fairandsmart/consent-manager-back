package com.fairandsmart.consent.manager.render;

import com.fairandsmart.consent.manager.model.Receipt;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.util.MimeConstants;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

@ApplicationScoped
public class PdfReceiptRenderer implements ReceiptRenderer {

    private static final TransformerFactory FACTORY = TransformerFactory.newInstance();
    private static final FopFactory FOP_FACTORY = FopFactory.newInstance(new File(".").toURI());
    private static final String STYLESHEET = "xsl/receipt.pdf.xsl";

    @Override
    public String format() {
        return "application/pdf";
    }

    @Override
    public byte[] render(Receipt receipt) throws RenderingException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
             InputStream style = this.getClass().getClassLoader().getResourceAsStream(STYLESHEET)) {
            FOUserAgent foUserAgent = FOP_FACTORY.newFOUserAgent();
            Fop fop = FOP_FACTORY.newFop(MimeConstants.MIME_PDF, foUserAgent, output);
            Transformer transformer = FACTORY.newTransformer(new StreamSource(style));
            transformer.setParameter("versionParam", "2.0");
            StreamSource xml = new StreamSource(new ByteArrayInputStream(receipt.toXmlBytes()));
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(xml, res);
            return output.toByteArray();
        } catch ( Exception e ) {
            throw new RenderingException("Unable to render receipt in application/pdf", e);
        }
    }
}
