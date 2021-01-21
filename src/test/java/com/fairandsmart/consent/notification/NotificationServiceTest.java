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
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@QuarkusTest
public class NotificationServiceTest {

    private static final Logger LOGGER = Logger.getLogger(NotificationServiceTest.class.getName());

    @Inject
    NotificationService service;

    @Test
    public void testConsentNotificationEvent() {
        LOGGER.log(Level.INFO, "Entering Test Submit Consent Event");
        Event event = new Event().withType(Event.CONSENT_SUBMIT).withAuthor("me").withArg("foo", "bar");
        service.notify(event);
    }

}
