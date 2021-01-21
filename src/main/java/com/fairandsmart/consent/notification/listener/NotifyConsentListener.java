package com.fairandsmart.consent.notification.listener;

/*-
 * #%L
 * Right Consents Community Edition
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * You accept that the Program was not created with a view to satisfy Your
 * individual requirements. Therefore, you must ensure that the Program
 * comply with Your needs, requirements and constraints. FAIR AND SMART
 * represents and warrants that it holds, without any restriction or
 * reservation, all the legal titles, authorizations and intellectual
 * property rights granted in the context of the GPLv3 License. See the
 * Additional Terms for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <https://www.gnu.org/licenses/>.
 * 
 * You should have received a copy of the Additional Terms along with this
 * program. If not, see <https://www.fairandsmart.com/opensource/>.
 * #L%
 */

import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.notification.entity.Event;
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

    @ConsumeEvent(value = Event.CONSENT_SUBMIT)
    public void consume(Event event) {
        LOGGER.log(Level.FINE, "Consent Submit event received: " + event.toString());
        ConsentContext ctx = (ConsentContext)event.getData();
        if (StringUtils.isNotEmpty(ctx.getNotificationRecipient())) {
            worker.setCtx(ctx);
            executor.submit(worker);
        }
    }
}
