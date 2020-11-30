package com.fairandsmart.consent.support;

public interface SupportService {

    String getSupportStatus();

    String getLatestVersion() throws SupportServiceException;

    String getCurrentVersion() throws SupportServiceException;

}
