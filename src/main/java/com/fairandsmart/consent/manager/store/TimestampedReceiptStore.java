package com.fairandsmart.consent.manager.store;

import com.fairandsmart.consent.common.config.MainConfig;
import com.fairandsmart.consent.common.exception.UnexpectedException;
import com.fairandsmart.consent.manager.ConsentReceipt;
import com.fairandsmart.quarkus.extension.timestamp.TimestampException;
import com.fairandsmart.quarkus.extension.timestamp.TimestampService;
import io.quarkus.arc.AlternativePriority;
import org.w3c.dom.Document;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@AlternativePriority(10)
public class TimestampedReceiptStore implements ReceiptStore {

    private static final Logger LOGGER = Logger.getLogger(TimestampedReceiptStore.class.getName());

    private static final String RECEIPT_STORE_HOME = "receipts";

    private static JAXBContext ctx;
    private static TransformerFactory tf;
    private static DocumentBuilderFactory dbf;

    @Inject
    MainConfig mainConfig;

    @Inject
    TimestampService timestampService;

    private Path base;

    @PostConstruct
    public void init() {
        String home = mainConfig.home();
        if ( home.startsWith("~") ) {
            home = System.getProperty("user.home") + home.substring(1);
            LOGGER.log(Level.INFO, "Setting service home relative to user home directory: " + home);
        }
        this.base = Paths.get(home, RECEIPT_STORE_HOME, mainConfig.instance());
        LOGGER.log(Level.INFO, "Initializing store with base folder: " + base);
        try {
            Files.createDirectories(base);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "unable to initialize receipt store", e);
        }
        try {
            tf = TransformerFactory.newInstance();
            dbf = DocumentBuilderFactory.newInstance();
            ctx = JAXBContext.newInstance(ConsentReceipt.class);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "unable to initialize receipt store", e);
        }
    }

    @Override
    public boolean exists(String id) {
        LOGGER.log(Level.FINE, "Checking if receipt exists for id: " + id);
        Path file = Paths.get(base.toString(), id);
        return Files.exists(file);
    }

    @Override
    public long size(String id) throws UnexpectedException, ReceiptNotFoundException {
        LOGGER.log(Level.FINE, "Getting size fo receipt with id: " + id);
        Path file = Paths.get(base.toString(), id);
        if ( !Files.exists(file) ) {
            throw new ReceiptNotFoundException("file not found in storage");
        }
        try {
            return Files.size(file);
        } catch (IOException e) {
            throw new UnexpectedException("unexpected error during stream size", e);
        }
    }

    @Override
    public void put(ConsentReceipt receipt) throws UnexpectedException, ReceiptAlreadyExistsException {
        Path file = Paths.get(base.toString(), receipt.getTransaction());
        if ( Files.exists(file) ) {
            throw new ReceiptAlreadyExistsException("unable to create file, id already exists");
        }
        try (FileWriter writer = new FileWriter(file.toString())) {
            Document document = dbf.newDocumentBuilder().newDocument();
            Marshaller marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(receipt, document);

            timestampService.timestamp(document);

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(writer);
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(source, result);
        } catch (IOException | JAXBException | ParserConfigurationException | TransformerException | TimestampException e) {
            throw new UnexpectedException("unexpected error during storing receipt", e);
        }
        LOGGER.log(Level.FINE, "New receipt stored with key: " + receipt.getTransaction());
    }

    @Override
    public ConsentReceipt get(String id) throws UnexpectedException, ReceiptNotFoundException {
        LOGGER.log(Level.FINE, "Getting receipt with id: " + id);
        Path file = Paths.get(base.toString(), id);
        if ( !Files.exists(file) ) {
            throw new ReceiptNotFoundException("file not found in storage");
        }
        try {
            Unmarshaller unmarshaller = ctx.createUnmarshaller();
            return (ConsentReceipt) unmarshaller.unmarshal(Files.newInputStream(file, StandardOpenOption.READ));
        } catch (IOException | JAXBException e) {
            throw new UnexpectedException("unexpected error while opening stream", e);
        }
    }

    @Override
    public void delete(String id) throws UnexpectedException, ReceiptNotFoundException {
        LOGGER.log(Level.FINE, "Deleting receipt with id: " + id);
        Path file = Paths.get(base.toString(), id);
        if ( !Files.exists(file) ) {
            throw new ReceiptNotFoundException("file not found in storage");
        }
        try {
            Files.delete(file);
        } catch (IOException e) {
            throw new UnexpectedException("unexpected error while deleting receipt", e);
        }
    }
}
