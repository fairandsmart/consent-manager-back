package com.fairandsmart.consent.support;

import io.quarkus.scheduler.Scheduled;

public interface SupportService {

    String getSupportStatus();

    String getLatestVersion() throws SupportServiceException;

    String getCurrentVersion() throws SupportServiceException;

    @Scheduled(cron="0 0 0 ? * MON")
    void checkLatestVersion();
}
