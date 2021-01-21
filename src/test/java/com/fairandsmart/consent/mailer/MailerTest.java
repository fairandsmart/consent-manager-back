package com.fairandsmart.consent.mailer;

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

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class MailerTest {

    private static final Logger LOGGER = Logger.getLogger(MailerTest.class.getName());

    @Inject
    Mailer mailer;

    @Inject
    MockMailbox mailbox;

    @BeforeEach
    void init() {
        mailbox.clear();
    }

    @Test
    public void testSendMail() {
        String address = "test@localhost";
        LOGGER.log(Level.INFO, "Sending email through mailer");
        mailer.send(Mail.withText(address, "Test Subject", "Test Body"));
        LOGGER.log(Level.INFO, "Checking mocked mailbox");
        List<Mail> sent = mailbox.getMessagesSentTo(address);
        assertEquals(1, sent.size());
        LOGGER.log(Level.INFO, "Mail sent !");
    }

}
