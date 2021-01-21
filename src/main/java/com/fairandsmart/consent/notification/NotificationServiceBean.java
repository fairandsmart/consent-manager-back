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

import com.fairandsmart.consent.notification.entity.Event;
import com.fairandsmart.consent.notification.entity.NotificationReport;
import io.vertx.mutiny.core.eventbus.EventBus;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class NotificationServiceBean implements NotificationService {

    private static final Logger LOGGER = Logger.getLogger(NotificationService.class.getName());

    @Inject
    EventBus bus;

    @Override
    public void notify(Event event) {
        LOGGER.log(Level.FINE, "Notify");
        bus.publish(event.getType(), event);
    }

    @Override
    public void pushReport(NotificationReport report) {
        Optional<NotificationReport> optionalReport = NotificationReport.find("transaction = ?1", report.transaction).firstResultOptional();
        if (optionalReport.isPresent()) {
            NotificationReport oldReport = optionalReport.get();
            if (oldReport.status.isValidTransition(report.status)) {
                oldReport.version += 1;
                oldReport.status = report.status;
                oldReport.explanation = report.explanation;
                oldReport.persist();
            } else {
                LOGGER.info("Could not update report for transaction " + report.transaction);
            }
        } else {
            report.persist();
        }
    }

    @Override
    public List<NotificationReport> listReports(String transaction) {
        return NotificationReport.list("transaction = ?1", transaction);
    }
}
