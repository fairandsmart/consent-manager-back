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

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class LocalReceiptStore implements ReceiptStore {

    private static final Logger LOGGER = Logger.getLogger(LocalReceiptStore.class.getName());

    private static final String RECEIPT_STORE_HOME = "receipts";

    @ConfigProperty(name = "consent.home")
    String home;

    private Path base;

    @PostConstruct
    public void init() {
        if ( home.startsWith("~") ) {
            home = System.getProperty("user.home") + home.substring(1);
            LOGGER.log(Level.INFO, "Setting service home relative to user home directory: " + home);
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
