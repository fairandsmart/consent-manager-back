package com.fairandsmart.consent.notification.worker;


import com.fairandsmart.consent.api.resource.ConsentsResource;
import com.fairandsmart.consent.common.config.ClientConfig;
import com.fairandsmart.consent.common.config.MainConfig;
import com.fairandsmart.consent.manager.*;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.exception.ModelDataSerializationException;
import com.fairandsmart.consent.manager.model.Email;
import com.fairandsmart.consent.notification.NotificationService;
import com.fairandsmart.consent.notification.entity.NotificationReport;
import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateService;
import com.fairandsmart.consent.template.TemplateServiceException;
import com.fairandsmart.consent.token.TokenService;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
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
    NotificationService notificationService;

    @Inject
    TemplateService templateService;

    @Inject
    ConsentService consentService;

    @Inject
    TokenService tokenService;

    @Inject
    MainConfig mainConfig;

    @Inject
    ClientConfig clientConfig;

    private ConsentContext ctx;

    public NotifyConsentWorker() {
        LOGGER.log(Level.INFO, "New consent worker created");
    }

    public void setCtx(ConsentContext ctx) {
        this.ctx = ctx;
    }

    @Override
    @Transactional
    public void run() {
        LOGGER.log(Level.FINE, "Notify Consent worker started for ctx: " + ctx);
        NotificationReport report = new NotificationReport(ctx.getReceiptId(), NotificationReport.Type.EMAIL, NotificationReport.Status.SENT);
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
            if (clientConfig.isUserPageEnabled() && clientConfig.userPagePublicUrl().isPresent()) {
                //TODO There is no pre authentication info on this link, we have to think about
                // - generate a token for accesing this page
                // - provide a secret for accessing page
                // - pass the username (email) allowing IdP account creation prefilled username...
                ctx.setCollectionMethod(ConsentContext.CollectionMethod.USER_PAGE);
                notification.setUrl(clientConfig.userPagePublicUrl().get());
            } else {
                ctx.setCollectionMethod(ConsentContext.CollectionMethod.EMAIL);
                notification.setToken(this.tokenService.generateToken(ctx,
                        Date.from(ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("UTC")).plus(ctx.getValidityInMillis(), ChronoUnit.MILLIS).toInstant())));
                URI notificationUri = UriBuilder.fromUri(mainConfig.publicUrl()).path(ConsentsResource.class).queryParam("t", notification.getToken()).build();
                notification.setUrl(notificationUri.toString());
            }
            if (ctx.getReceiptDeliveryType().equals(ConsentContext.ReceiptDeliveryType.DOWNLOAD) && StringUtils.isNotEmpty(ctx.getReceiptId())) {
                notification.setReceiptName("receipt.pdf");
                notification.setReceiptType("application/pdf");
                notification.setReceipt(this.consentService.systemRenderReceipt(ctx.getReceiptId(), "application/pdf", ctx.getTheme()));
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
            report.status = NotificationReport.Status.ERROR;
            report.explanation = e.getMessage();
        } catch (TemplateServiceException e) {
            LOGGER.log(Level.SEVERE, "error while calculating template for email", e);
            report.status = NotificationReport.Status.ERROR;
            report.explanation = e.getMessage();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "unexpected error", e);
            report.status = NotificationReport.Status.ERROR;
            report.explanation = e.getMessage();
        }
        this.notificationService.pushReport(report);
    }
}
