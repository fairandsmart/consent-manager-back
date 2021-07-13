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

import com.fairandsmart.consent.notification.entity.Event;
import com.fairandsmart.consent.notification.worker.PersistEventWorker;
import io.quarkus.vertx.ConsumeEvent;
import org.eclipse.microprofile.context.ManagedExecutor;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class PersistEventListener {

    private static final Logger LOGGER = Logger.getLogger(PersistEventListener.class.getName());

    @Inject
    ManagedExecutor executor;

    @Inject
    PersistEventWorker worker;

    @ConsumeEvent(value = Event.AUDIT_CHANNEL)
    public void consume(Event event) {
        LOGGER.log(Level.FINE, "Event received: " + event.toString());
        worker.setEvent(event);
        executor.submit(worker);
    }

}
