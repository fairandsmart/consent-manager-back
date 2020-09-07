package com.fairandsmart.consent.timestamp;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

@QuarkusTest
public class TimestampServiceTest {

    private static final Logger LOGGER = Logger.getLogger(TimestampServiceTest.class.getName());

    @Inject
    TimestampService timestampService;

    @Test
    public void testTimestamp() throws ParserConfigurationException, IOException, SAXException, TimestampServiceException, TransformerException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(this.getClass().getClassLoader().getResourceAsStream("receipt/receipt.xml"));
        timestampService.timestamp(doc);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        LOGGER.log(Level.INFO, writer.getBuffer().toString());
    }

}
