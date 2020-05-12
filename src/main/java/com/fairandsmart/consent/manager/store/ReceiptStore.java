package com.fairandsmart.consent.manager.store;

import java.io.InputStream;

public interface ReceiptStore {

    boolean exists(String key) throws ReceiptStoreException;

    String put(InputStream is) throws ReceiptStoreException;

    long size(String key) throws ReceiptStoreException, ReceiptNotFoundException;

    InputStream get(String key) throws ReceiptStoreException, ReceiptNotFoundException;

    void delete(String key) throws ReceiptStoreException, ReceiptNotFoundException;

}
