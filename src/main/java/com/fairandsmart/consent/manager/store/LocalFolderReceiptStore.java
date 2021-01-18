package com.fairandsmart.consent.manager.store;

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

import com.fairandsmart.consent.common.config.MainConfig;
import com.fairandsmart.consent.manager.model.Receipt;

import javax.annotation.PostConstruct;
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
