package com.fairandsmart.consent.notification.listener;

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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class SubmitConsentListener {

    private static final Logger LOGGER = Logger.getLogger(SubmitConsentListener.class.getName());

    @Inject
    ReactiveMailer mailer;

    @Inject
    TemplateService template;

    @ConsumeEvent(value = Event.CONSENT_NOTIFICATION)
    public void consume(Event event) {
        LOGGER.log(Level.FINE, "Submit Consent event received: " + event.toString());
        try {
            if (event.getData() != null) {
                ConsentNotification notification = (ConsentNotification) event.getData();
                TemplateModel<ConsentNotification> model = new TemplateModel<>("email.ftl", notification, LocaleUtils.toLocale(notification.getLocale()));
                ResourceBundle bundle = ResourceBundle.getBundle("freemarker/bundles/consent", model.getLocale());
                model.setBundle(bundle);

                LOGGER.log(Level.FINE, "Rendering body");
                String body = template.render(model);

                LOGGER.log(Level.FINE, "Sending message to: " + notification.getRecipient());
                String subject = ((Email) notification.getModel().getData(notification.getLocale())).getSubject();
                String sender = ((Email) notification.getModel().getData(notification.getLocale())).getSender();
                Mail mail = Mail.withHtml(notification.getRecipient(), subject, body).setFrom(sender);
                if (notification.getReceiptName() != null) {
                    LOGGER.log(Level.FINE, "Adding receipt attachment to message");
                    mail.addAttachment(notification.getReceiptName(), notification.getReceipt(), notification.getReceiptType());
                }
                mailer.send(mail).subscribeAsCompletionStage();
            } else {
                LOGGER.log(Level.FINE, "no data found in event, nothing to notify...");
            }
        } catch (ClassCastException e) {
            LOGGER.log(Level.SEVERE, "event data cannot be read as a ConsentNotification", e);
        } catch (ModelDataSerializationException e) {
            LOGGER.log(Level.SEVERE, "unable to read model data", e);
        } catch (TemplateServiceException e) {
            LOGGER.log(Level.SEVERE, "error while calculating template for email", e);
        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, "unexpected error", e);
        }
    }
}
