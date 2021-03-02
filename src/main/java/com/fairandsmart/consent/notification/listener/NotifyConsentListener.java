package com.fairandsmart.consent.notification.listener;

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

import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.notification.entity.Event;
import com.fairandsmart.consent.notification.entity.EventType;
import com.fairandsmart.consent.notification.worker.NotifyConsentWorker;
import io.quarkus.vertx.ConsumeEvent;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.context.ManagedExecutor;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class NotifyConsentListener {

    private static final Logger LOGGER = Logger.getLogger(NotifyConsentListener.class.getName());

    @Inject
    ManagedExecutor executor;

    @Inject
    NotifyConsentWorker worker;

    @ConsumeEvent(value = Event.NOTIFICATION_CHANNEL)
    public void consume(Event event) {
        LOGGER.log(Level.FINE, "Event received: " + event.toString());
        if (event.getEventType().equals(EventType.CONSENT_SUBMIT)) {
            ConsentContext ctx = (ConsentContext) event.getData();
            if (StringUtils.isNotEmpty(ctx.getNotificationRecipient())) {
                worker.setCtx(ctx);
                executor.submit(worker);
            }
        }
    }
}
