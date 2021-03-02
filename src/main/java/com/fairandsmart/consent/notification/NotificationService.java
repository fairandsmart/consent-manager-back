package com.fairandsmart.consent.notification;

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

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.notification.entity.Event;
import com.fairandsmart.consent.notification.entity.NotificationReport;
import com.fairandsmart.consent.notification.filter.EventFilter;

import java.util.List;
import java.util.Map;

public interface NotificationService {

    void publish(Event event);

    void publish(String eventType, String sourceType, String sourceId, String author);

    void publish(String eventType, String sourceType, String sourceId, String author, Map<String, String> args);

    void pushReport(NotificationReport report);

    CollectionPage<Event> findEvents(EventFilter filter);

    List<NotificationReport> listReports(String transactionId);

}
