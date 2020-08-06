package com.fairandsmart.consent.notification;

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
    public void testCreateRecordEvent() {
        LOGGER.log(Level.INFO, "Entering Test Create Record Event");
        service.notify(new Event().withType(Event.SUBMIT_CONSENT).withAuthor("me").withArg("foo", "bar"));

        //Find a way to test that the event is really treated...
    }


}
