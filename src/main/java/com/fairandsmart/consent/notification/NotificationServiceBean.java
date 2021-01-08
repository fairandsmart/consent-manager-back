package com.fairandsmart.consent.notification;

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
