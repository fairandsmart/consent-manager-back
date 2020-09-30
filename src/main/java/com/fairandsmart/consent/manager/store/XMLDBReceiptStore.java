package com.fairandsmart.consent.manager.store;

import io.quarkus.runtime.Startup;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.exist.storage.BrokerPool;
import org.exist.util.Configuration;
import org.exist.xmldb.DatabaseInstanceManager;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
//@Startup
public class XMLDBReceiptStore implements ReceiptStore {

    private static final Logger LOGGER = Logger.getLogger(XMLDBReceiptStore.class.getName());

    private static final String XMLDB_DRIVER = "org.exist.xmldb.DatabaseImpl";
    //private static final String XMLDB_BASE_COLLECTION = "xmldb:exist://localhost:9090/xmlrpc/db";
    private static final String XMLDB_BASE_COLLECTION = "xmldb:exist:///db";
    private static final String COLLECTION_NAME = "qualipso";
    private static final String RECEIPT_STORE_HOME = "xmldb";

    @ConfigProperty(name = "consent.home")
    String home;

    private Path base;
    private Collection baseCollection;
    private Collection collection;

    @PostConstruct
    public void init() throws ReceiptStoreException {
        LOGGER.log(Level.INFO, "Initializing XMLDBReceiptStore");
        try {
            if ( home.startsWith("~") ) {
                home = System.getProperty("user.home") + home.substring(1);
                LOGGER.log(Level.INFO, "Setting service home relative to user home directory: " + home);
            }
            this.base = Paths.get(home, RECEIPT_STORE_HOME);
            Path data = Paths.get(base.toString(), "data");
            LOGGER.log(Level.INFO, "Initializing store with base folder: " + base);
            Files.createDirectories(data);
            System.setProperty("exist.home", base.toString());

            Path config = Paths.get(base.toString(), "conf.xml");
            LOGGER.log(Level.INFO,"config file set to " + config);
            if (!Files.exists(config)) {
                LOGGER.log(Level.INFO,"Config file does not exist, creating default configuration...");
                InputStream defaultConfig = this.getClass().getClassLoader().getResourceAsStream("conf.xml");
                Files.copy(defaultConfig, config);
            }
            if (!Files.isReadable(config)) {
                throw new Exception("configuration file " + config + " is not readable");
            }
            Configuration configuration = new Configuration(config.toString());
            if (configuration == null) {
                throw new Exception("Failed to create configuration for database");
            }
            if (!BrokerPool.isConfigured()) {
                LOGGER.log(Level.INFO,"Configuring database");
                BrokerPool.configure(1, 5, configuration);
            }

            Class cl = Class.forName(XMLDB_DRIVER);
            Database database = (Database) cl.getDeclaredConstructor().newInstance();
            database.setProperty("create-database", "true");
            DatabaseManager.registerDatabase(database);

            baseCollection = DatabaseManager.getCollection(XMLDB_BASE_COLLECTION, "admin", "");
            baseCollection.setProperty("encoding", "ISO-8859-1");

            LOGGER.log(Level.FINE, "Got Base Collection");

            String[] collections = baseCollection.listChildCollections();
            boolean collectionExists = false;

            for (String collection : collections) {
                if (collection.equals(COLLECTION_NAME)) {
                    collectionExists = true;
                    break;
                }
            }

            if (!collectionExists) {
                CollectionManagementService service = (CollectionManagementService) baseCollection.getService("CollectionManagementService", "1.0");
                collection = service.createCollection(COLLECTION_NAME);
            } else {
                collection = baseCollection.getChildCollection(COLLECTION_NAME);
            }

        } catch (Exception e) {
            throw new ReceiptStoreException("Unable to initialize XMLDBReceiptStore Store", e) ;
        }
    }

    @PreDestroy
    public void shutdown() {
        LOGGER.log(Level.INFO, "Shutting down XMLDBReceiptStore");
        try {
            DatabaseInstanceManager manager = (DatabaseInstanceManager) collection.getService("DatabaseInstanceManager", "1.0");
            manager.shutdown();
        } catch ( Exception e ) {
            LOGGER.log(Level.SEVERE, "Unable to shutdown XMLDB Store properly", e);
        }
    }

    @Override
    public boolean exists(String key) {
        LOGGER.log(Level.FINE, "Checking if key exists: " + key);
        try {
            return collection.getResource(key) != null;
        } catch ( Exception e ) {
            LOGGER.log(Level.SEVERE, "error during testing if receipt exists");
            return false;
        }
    }

    @Override
    public void put(String key, InputStream is) throws ReceiptStoreException, ReceiptAlreadyExistsException {
        LOGGER.log(Level.FINE, "Adding receipt with key; " + key);
        try {
            if ( collection.getResource(key) != null ) {
                throw new ReceiptAlreadyExistsException("A recipt already exists storage for key: " + key);
            }
            XMLResource document = (XMLResource) collection.createResource(key, "XMLResource");
            document.setContent(is.readAllBytes());
            collection.storeResource(document);
            LOGGER.log(Level.FINE, "receipt stored");
        } catch (XMLDBException | IOException e) {
            throw new ReceiptStoreException("unable to store receipt ", e);
        }
    }

    @Override
    public void put(String key, byte[] input) throws ReceiptStoreException, ReceiptAlreadyExistsException {
        LOGGER.log(Level.FINE, "Adding receipt with key; " + key);
        try {
            if ( collection.getResource(key) != null ) {
                throw new ReceiptAlreadyExistsException("A recipt already exists storage for key: " + key);
            }
            XMLResource document = (XMLResource) collection.createResource(key, "XMLResource");
            document.setContent(input);
            collection.storeResource(document);
            LOGGER.log(Level.FINE, "receipt stored");
        } catch (XMLDBException e) {
            throw new ReceiptStoreException("unable to store receipt ", e);
        }
    }

    @Override
    public long size(String key) throws ReceiptStoreException, ReceiptNotFoundException {
        LOGGER.log(Level.FINE, "Getting receipt with key; " + key);
        try {
            XMLResource document = (XMLResource) collection.getResource(key);
            if ( document == null ) {
                throw new ReceiptNotFoundException("Unable to find a receipt with key: " + key);
            }
            return ((String) document.getContent()).length();
        } catch (XMLDBException e) {
            throw new ReceiptStoreException("unable to get size for receipt ", e);
        }
    }

    @Override
    public InputStream get(String key) throws ReceiptStoreException, ReceiptNotFoundException {
        LOGGER.log(Level.FINE, "Getting receipt with key; " + key);
        try {
            XMLResource document = (XMLResource) collection.getResource(key);
            if ( document == null ) {
                throw new ReceiptNotFoundException("Unable to find a receipt with key: " + key);
            }
            byte[] receipt = ((String) document.getContent()).getBytes();
            return new ByteArrayInputStream(receipt);
        } catch (XMLDBException e) {
            throw new ReceiptStoreException("unable to get receipt ", e);
        }
    }

    @Override
    public void delete(String key) throws ReceiptStoreException, ReceiptNotFoundException {
        LOGGER.log(Level.FINE, "Deleting receipt with key; " + key);
        try {
            XMLResource document = (XMLResource) collection.getResource(key);
            if ( document == null ) {
                throw new ReceiptNotFoundException("Unable to find a receipt with key: " + key);
            }
            collection.removeResource(document);
        } catch (XMLDBException e) {
            throw new ReceiptStoreException("unable to delete receipt ", e);
        }
    }

}
