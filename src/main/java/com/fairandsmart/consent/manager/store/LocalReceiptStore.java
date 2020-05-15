package com.fairandsmart.consent.manager.store;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class LocalReceiptStore implements ReceiptStore {

    private static final Logger LOGGER = Logger.getLogger(LocalReceiptStore.class.getName());
    public static final String RECEIPT_STORE_HOME = "receipts";


    @ConfigProperty(name = "consent.home")
    public String home;

    private Path base;

    @PostConstruct
    public void init() {
        if ( home.contains("~") ) {
            home.replaceFirst("~", System.getProperty("user.home"));
        }
        this.base = Paths.get(home, RECEIPT_STORE_HOME);
        LOGGER.log(Level.INFO, "Initializing store with base folder: " + base);
        try {
            Files.createDirectories(base);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "unable to initialize receipt store", e);
        }
    }

    @Override
    public boolean exists(String key) {
        LOGGER.log(Level.FINE, "Checking if key exists: " + key);
        Path file = Paths.get(base.toString(), key);
        return Files.exists(file);
    }

    @Override
    public long size(String key) throws ReceiptStoreException, ReceiptNotFoundException {
        LOGGER.log(Level.FINE, "Getting size fo key: " + key);
        Path file = Paths.get(base.toString(), key);
        if ( !Files.exists(file) ) {
            throw new ReceiptNotFoundException("file not found in storage");
        }
        try {
            return Files.size(file);
        } catch (IOException e) {
            throw new ReceiptStoreException("unexpected error during stream size", e);
        }
    }

    @Override
    public void put(String key, byte[] input) throws ReceiptStoreException, ReceiptAlreadyExistsException {
        try (InputStream is = new ByteArrayInputStream(input)) {
            this.put(key, is);
        } catch (IOException e) {
            throw new ReceiptStoreException("unexpected error during stream copy", e);
        }
    }

    @Override
    public void put(String key, InputStream is) throws ReceiptStoreException, ReceiptAlreadyExistsException {
        Path file = Paths.get(base.toString(), key);
        if ( Files.exists(file) ) {
            throw new ReceiptAlreadyExistsException("unable to create file, key already exists");
        }
        try {
            Files.copy(is, file, StandardCopyOption.REPLACE_EXISTING);
            is.close();
        } catch (IOException e) {
            throw new ReceiptStoreException("unexpected error during stream copy", e);
        }
        LOGGER.log(Level.FINE, "New content stored with key: " + key);
    }

    @Override
    public InputStream get(String key) throws ReceiptStoreException, ReceiptNotFoundException {
        LOGGER.log(Level.FINE, "Getting content with key: " + key);
        Path file = Paths.get(base.toString(), key);
        if ( !Files.exists(file) ) {
            throw new ReceiptNotFoundException("file not found in storage");
        }
        try {
            return Files.newInputStream(file, StandardOpenOption.READ);
        } catch (IOException e) {
            throw new ReceiptStoreException("unexpected error while opening stream", e);
        }
    }

    @Override
    public void delete(String key) throws ReceiptStoreException, ReceiptNotFoundException {
        LOGGER.log(Level.FINE, "Deleting content with key: " + key);
        Path file = Paths.get(base.toString(), key);
        if ( !Files.exists(file) ) {
            throw new ReceiptNotFoundException("file not found in storage");
        }
        try {
            Files.delete(file);
        } catch (IOException e) {
            throw new ReceiptStoreException("unexpected error while deleting stream", e);
        }
    }
}
