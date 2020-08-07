package com.fairandsmart.consent.notification.listener;

import com.fairandsmart.consent.common.exception.EntityNotFoundException;
import com.fairandsmart.consent.manager.ConsentOptOut;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.manager.ModelDataSerializationException;
import com.fairandsmart.consent.manager.model.Email;
import com.fairandsmart.consent.notification.entity.Event;
import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateService;
import com.fairandsmart.consent.template.TemplateServiceException;
import com.fairandsmart.consent.token.InvalidTokenException;
import com.fairandsmart.consent.token.TokenExpiredException;
import com.fairandsmart.consent.token.TokenService;
import com.fairandsmart.consent.token.TokenServiceException;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
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

    @ConsumeEvent(value = Event.CONSENT_OPTOUT)
    public void consume(Event event) {
        LOGGER.log(Level.FINE, "Submit Consent event received: " + event.toString());
        try {
            if (event.getData() != null) {
                ConsentOptOut optout = (ConsentOptOut) event.getData();
                TemplateModel<ConsentOptOut> model = new TemplateModel<>("email.ftl", optout, LocaleUtils.toLocale(optout.getLocale()));
                ResourceBundle bundle = ResourceBundle.getBundle("freemarker/bundles/consent", model.getLocale());
                model.setBundle(bundle);

                LOGGER.log(Level.FINE, "Rendering body");
                String body = template.render(model);

                LOGGER.log(Level.FINE, "Sending message to: " + optout.getRecipient());
                String subject = ((Email) optout.getModel().getData(optout.getLocale())).getSubject();
                String sender = ((Email) optout.getModel().getData(optout.getLocale())).getSender();
                mailer.send(Mail.withHtml(optout.getRecipient(), subject, body).setFrom(sender)).subscribeAsCompletionStage();
            } else {
                LOGGER.log(Level.FINE, "token is missing in event, nothing to notify...");
            }
        } catch (ClassCastException e) {
            LOGGER.log(Level.SEVERE, "event data cannot be readed as a ConsentOptOut", e);
        } catch (ModelDataSerializationException e) {
            LOGGER.log(Level.SEVERE, "unable to read model data", e);
        } catch (TemplateServiceException e) {
            LOGGER.log(Level.SEVERE, "error while calculating template for email", e);
        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, "unexpected error", e);
        }
    }
}
