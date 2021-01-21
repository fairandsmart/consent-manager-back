package com.fairandsmart.consent.support;

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

import com.fairandsmart.consent.support.entity.Instance;
import io.quarkus.scheduler.Scheduled;

public interface SupportService {

    Instance getInstance();

    String getSupportStatus();

    String getLatestVersion() throws SupportServiceException;

    String getCurrentVersion() throws SupportServiceException;

    void init() throws SupportServiceException;

    @Scheduled(cron="0 0 0 ? * MON")
    void refresh();
}
