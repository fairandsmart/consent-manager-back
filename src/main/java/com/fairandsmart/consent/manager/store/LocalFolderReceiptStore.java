package com.fairandsmart.consent.manager.store;

/*-
 * #%L
 * Right Consents, a Universal Consents & Preferences Management Platform.
 * %%
 * Copyright (C) 2020 - 2021 Fair And Smart
 * %%
 * This file is part of Right Consents Community Edition.
 * 
 * Right Consents Community Edition is published by FAIR AND SMART under the
 * GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 * 
 * For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 * files, or see https://www.fairandsmart.com/opensource/.
 * #L%
 */

import com.fairandsmart.consent.common.config.MainConfig;
import com.fairandsmart.consent.manager.model.Receipt;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Default
public class LocalFolderReceiptStore implements ReceiptStore {

    private static final Logger LOGGER = Logger.getLogger(LocalFolderReceiptStore.class.getName());

    private static final String RECEIPT_STORE_HOME = "receipts";

    private static JAXBContext ctx;

    @Inject
    MainConfig mainConfig;

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
            ctx = JAXBContext.newInstance(Receipt.class);
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
    public long size(String id) throws ReceiptStoreException, ReceiptNotFoundException {
        LOGGER.log(Level.FINE, "Getting size fo receipt with id: " + id);
        Path file = Paths.get(base.toString(), id);
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
    public void put(Receipt receipt) throws ReceiptStoreException, ReceiptAlreadyExistsException {
        Path file = Paths.get(base.toString(), receipt.getTransaction());
        if ( Files.exists(file) ) {
            throw new ReceiptAlreadyExistsException("unable to create file, id already exists");
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Marshaller marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(receipt, out);
            Files.write(file, out.toByteArray());
        } catch (IOException | JAXBException e) {
            throw new ReceiptStoreException("unexpected error during stream copy", e);
        }
        LOGGER.log(Level.FINE, "New receipt stored with key: " + receipt.getTransaction());
    }

    @Override
    public Receipt get(String id) throws ReceiptStoreException, ReceiptNotFoundException {
        LOGGER.log(Level.FINE, "Getting receipt with id: " + id);
        Path file = Paths.get(base.toString(), id);
        if ( !Files.exists(file) ) {
            throw new ReceiptNotFoundException("file not found in storage");
        }
        try {
            Unmarshaller unmarshaller = ctx.createUnmarshaller();
            return (Receipt) unmarshaller.unmarshal(Files.newInputStream(file, StandardOpenOption.READ));
        } catch (IOException | JAXBException e) {
            throw new ReceiptStoreException("unexpected error while opening stream", e);
        }
    }

    @Override
    public void delete(String id) throws ReceiptStoreException, ReceiptNotFoundException {
        LOGGER.log(Level.FINE, "Deleting receipt with id: " + id);
        Path file = Paths.get(base.toString(), id);
        if ( !Files.exists(file) ) {
            throw new ReceiptNotFoundException("file not found in storage");
        }
        try {
            Files.delete(file);
        } catch (IOException e) {
            throw new ReceiptStoreException("unexpected error while deleting receipt", e);
        }
    }
}
