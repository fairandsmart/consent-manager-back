package com.fairandsmart.consent.notification.worker;


import com.fairandsmart.consent.api.resource.ConsentsResource;
import com.fairandsmart.consent.common.config.MainConfig;
import com.fairandsmart.consent.manager.*;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.model.Email;
import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateService;
import com.fairandsmart.consent.template.TemplateServiceException;
import com.fairandsmart.consent.token.TokenService;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Dependent
public class NotifyConsentWorker implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(NotifyConsentWorker.class.getName());

    @Inject
    Mailer mailer;

    @Inject
    TemplateService templateService;

    @Inject
    ConsentService consentService;

    @Inject
    TokenService tokenService;

    @Inject
    MainConfig config;

    private ConsentContext ctx;

    public NotifyConsentWorker() {
        LOGGER.log(Level.INFO, "New consent worker created");
    }

    public void setCtx(ConsentContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void run() {
        LOGGER.log(Level.FINE, "Notify Consent worker started for ctx: " + ctx);
        try {
            ConsentNotification notification = new ConsentNotification();
            notification.setLanguage(ctx.getLanguage());
            notification.setRecipient(ctx.getNotificationRecipient());
            ModelVersion notificationModel = ModelVersion.SystemHelper.findModelVersionForSerial(ConsentElementIdentifier.deserialize(ctx.getNotificationModel()).getSerial(), true);
            notification.setModel(notificationModel);
            if (!StringUtils.isEmpty(ctx.getTheme())) {
                ModelVersion theme = ModelVersion.SystemHelper.findModelVersionForSerial(ConsentElementIdentifier.deserialize(ctx.getTheme()).getSerial(), true);
                notification.setTheme(theme);
            }
            ctx.setCollectionMethod(ConsentContext.CollectionMethod.EMAIL);
            notification.setToken(this.tokenService.generateToken(ctx));
            URI notificationUri = UriBuilder.fromUri(config.publicUrl()).path(ConsentsResource.class).queryParam("t", notification.getToken()).build();
            notification.setUrl(notificationUri.toString());
            if (ctx.getReceiptDeliveryType().equals(ConsentContext.ReceiptDeliveryType.DOWNLOAD) && StringUtils.isNotEmpty(ctx.getReceiptId())) {
                notification.setReceiptName("receipt.pdf");
                notification.setReceiptType("application/pdf");
                notification.setReceipt(this.consentService.systemRenderReceipt(ctx.getReceiptId(), "application/pdf"));
            }

            TemplateModel<ConsentNotification> model = new TemplateModel("email.ftl", notification, notification.getLanguage());
            ResourceBundle bundle = ResourceBundle.getBundle("freemarker/bundles/consent", Locale.forLanguageTag(model.getLanguage()));
            model.setBundle(bundle);

            LOGGER.log(Level.FINE, "Rendering body");
            String body = templateService.render(model);

            String subject = ((Email) notification.getModel().getData(notification.getLanguage())).getSubject();
            String sender = ((Email) notification.getModel().getData(notification.getLanguage())).getSender();
            LOGGER.log(Level.FINE, "Sending message to: " + notification.getRecipient() + " with sender: " + sender);
            Mail mail = Mail.withHtml(notification.getRecipient(), subject, body).setFrom(sender);
            if (notification.getReceiptName() != null) {
                LOGGER.log(Level.FINE, "Adding receipt attachment to message");
                mail.addAttachment(notification.getReceiptName(), notification.getReceipt(), notification.getReceiptType());
            }
            mailer.send(mail);
        } catch (ModelDataSerializationException e) {
            LOGGER.log(Level.SEVERE, "unable to read email model data", e);
        } catch (TemplateServiceException e) {
            LOGGER.log(Level.SEVERE, "error while calculating template for email", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "unexpected error", e);
        }
    }
}