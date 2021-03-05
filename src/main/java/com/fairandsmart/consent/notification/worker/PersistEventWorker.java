package com.fairandsmart.consent.notification.worker;

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

import javax.enterprise.context.Dependent;
import javax.transaction.Transactional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Dependent
public class PersistEventWorker implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(PersistEventWorker.class.getName());

    private Event event;

    public PersistEventWorker() {
        LOGGER.log(Level.INFO, "New persist event worker created");
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    @Transactional
    public void run() {
        try {
            this.event.persist();
            LOGGER.log(Level.FINE, "Event persisted with id: " + event.getId());
        } catch ( Exception e ) {
            LOGGER.log(Level.WARNING, "shit happens", e);
        }
    }
}
