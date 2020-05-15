package com.fairandsmart.consent.manager.store;

import java.io.InputStream;

public interface ReceiptStore {

    boolean exists(String key) throws ReceiptStoreException;

    void put(String key, InputStream is) throws ReceiptStoreException, ReceiptAlreadyExistsException;

    void put(String key, byte[] input) throws ReceiptStoreException, ReceiptAlreadyExistsException;

    long size(String key) throws ReceiptStoreException, ReceiptNotFoundException;

    InputStream get(String key) throws ReceiptStoreException, ReceiptNotFoundException;

    void delete(String key) throws ReceiptStoreException, ReceiptNotFoundException;

}
