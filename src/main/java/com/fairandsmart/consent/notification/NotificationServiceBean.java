package com.fairandsmart.consent.notification;

import com.fairandsmart.consent.notification.entity.Event;
import io.vertx.mutiny.core.eventbus.EventBus;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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

}
