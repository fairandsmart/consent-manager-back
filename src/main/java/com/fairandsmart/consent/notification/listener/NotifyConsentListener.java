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

import com.fairandsmart.consent.manager.ConsentNotification;
import com.fairandsmart.consent.manager.ModelDataSerializationException;
import com.fairandsmart.consent.manager.model.Email;
import com.fairandsmart.consent.notification.entity.Event;
import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateService;
import com.fairandsmart.consent.template.TemplateServiceException;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.quarkus.vertx.ConsumeEvent;
import org.apache.commons.lang3.LocaleUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class NotifyConsentListener {

    private static final Logger LOGGER = Logger.getLogger(NotifyConsentListener.class.getName());

    @Inject
    ReactiveMailer mailer;

    @Inject
    TemplateService templateService;

    @ConsumeEvent(value = Event.CONSENT_NOTIFY)
    public void consume(Event event) {
        LOGGER.log(Level.FINE, "Consent Notify event received: " + event.toString());
        try {
            ConsentNotification notification = (ConsentNotification) event.getData();
            TemplateModel<ConsentNotification> model = new TemplateModel("email.ftl", notification, notification.getLanguage());
            ResourceBundle bundle = ResourceBundle.getBundle("freemarker/bundles/consent", Locale.forLanguageTag(model.getLanguage()));
            model.setBundle(bundle);

            LOGGER.log(Level.FINE, "Rendering body");
            String body = templateService.render(model);

            LOGGER.log(Level.FINE, "Sending message to: " + notification.getRecipient());
            String subject = ((Email) notification.getModel().getData(notification.getLanguage())).getSubject();
            String sender = ((Email) notification.getModel().getData(notification.getLanguage())).getSender();
            Mail mail = Mail.withHtml(notification.getRecipient(), subject, body).setFrom(sender);
            if (notification.getReceiptName() != null) {
                LOGGER.log(Level.FINE, "Adding receipt attachment to message");
                mail.addAttachment(notification.getReceiptName(), notification.getReceipt(), notification.getReceiptType());
            }
            mailer.send(mail).subscribeAsCompletionStage();
        } catch (ModelDataSerializationException e) {
            LOGGER.log(Level.SEVERE, "unable to read email model data", e);
        } catch (TemplateServiceException e) {
            LOGGER.log(Level.SEVERE, "error while calculating template for email", e);
        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, "unexpected error", e);
        }
    }
}
