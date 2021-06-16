package com.fairandsmart.consent.manager.confirmation;

public interface ConfirmationManager {

    String generate();

    void begin(String id);

    void commit(String id);

}
