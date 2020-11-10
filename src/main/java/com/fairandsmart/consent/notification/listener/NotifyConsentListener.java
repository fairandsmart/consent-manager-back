package com.fairandsmart.consent.notification.listener;

/*-
 * #%L
 * Right Consent / A Consent Manager Platform
 * 
 * Authors:
 * 
 * Xavier Lefevre <xavier.lefevre@fairandsmart.com> / FairAndSmart
 * Nicolas Rueff <nicolas.rueff@fairandsmart.com> / FairAndSmart
 * Jérôme Blanchard <jerome.blanchard@fairandsmart.com> / FairAndSmart
 * Alan Balbo <alan.balbo@fairandsmart.com> / FairAndSmart
 * Frederic Pierre <frederic.pierre@fairansmart.com> / FairAndSmart
 * Victor Guillaume <victor.guillaume@fairandsmart.com> / FairAndSmart
 * Manon Stremplewski <manon.stremplewski@fairandsmart.com> / FairAndSmart
 * Pauline Kullmann <pauline.kullmmann@fairandsmart.com> / FairAndSmart
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.ConsentNotification;
import com.fairandsmart.consent.manager.ModelDataSerializationException;
import com.fairandsmart.consent.manager.model.Email;
import com.fairandsmart.consent.notification.entity.Event;
import com.fairandsmart.consent.notification.worker.NotifyConsentWorker;
import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateService;
import com.fairandsmart.consent.template.TemplateServiceException;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.quarkus.vertx.ConsumeEvent;
import org.apache.commons.lang3.LocaleUtils;
import org.eclipse.microprofile.context.ManagedExecutor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.Locale;
import java.util.ResourceBundle;
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
        worker.setCtx(ctx);
        executor.submit(worker);
    }
}
