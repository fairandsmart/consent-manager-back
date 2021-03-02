package com.fairandsmart.consent.notification.listener;

import com.fairandsmart.consent.notification.entity.Event;
import com.fairandsmart.consent.notification.worker.PersistEventWorker;
import io.quarkus.vertx.ConsumeEvent;
import org.eclipse.microprofile.context.ManagedExecutor;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
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
