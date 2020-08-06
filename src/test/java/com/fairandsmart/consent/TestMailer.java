package com.fairandsmart.consent;

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
public class TestMailer {

    private static final Logger LOGGER = Logger.getLogger(TestMailer.class.getName());

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
        LOGGER.log(Level.INFO, "Sending email throught mailer");
        mailer.send(Mail.withText("jerome@localhost", "Test SUbject", "Test Body"));
        LOGGER.log(Level.INFO, "Mail sent, checking mocked mailbox");
        List<Mail> sent = mailbox.getMessagesSentTo("jerome@localhost");
        assertEquals(1, sent.size());
        LOGGER.log(Level.INFO, "Mail received !");
    }


}
