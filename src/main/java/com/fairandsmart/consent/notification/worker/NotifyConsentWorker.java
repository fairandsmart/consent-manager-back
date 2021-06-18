package com.fairandsmart.consent.notification.worker;

/*-
 * #%L
 * Right Consents, a Universal Consents & Preferences Management Platform.
 * %%
 * Copyright (C) 2020 - 2021 Fair And Smart
 * %%
 * This file is part of Right Consents Community Edition.
 * 
 * Right Consents Community Edition is published by FAIR AND SMART under the
 * GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 * 
 * For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 * files, or see https://www.fairandsmart.com/opensource/.
 * #L%
 */

import com.fairandsmart.consent.api.resource.ConsentsResource;
import com.fairandsmart.consent.common.config.ClientConfig;
import com.fairandsmart.consent.common.config.MainConfig;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.ConsentElementIdentifier;
import com.fairandsmart.consent.manager.ConsentNotification;
import com.fairandsmart.consent.manager.ConsentService;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.exception.ModelDataSerializationException;
import com.fairandsmart.consent.manager.model.Email;
import com.fairandsmart.consent.manager.store.ReceiptNotFoundException;
import com.fairandsmart.consent.notification.NotificationService;
import com.fairandsmart.consent.notification.entity.NotificationReport;
import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateService;
import com.fairandsmart.consent.template.TemplateServiceException;
import com.fairandsmart.consent.token.AccessToken;
import com.fairandsmart.consent.token.TokenService;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.transaction.Transactional;
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

    private String txid;
    private ConsentContext ctx;

    public NotifyConsentWorker() {
        LOGGER.log(Level.INFO, "New consent worker created");
    }

    public void setCtx(ConsentContext ctx) {
        this.ctx = ctx;
    }

    public void setTransactionId(String txid) {
        this.txid = txid;
    }

    @Override
    @Transactional
    public void run() {
        LOGGER.log(Level.FINE, "Notify Consent worker started for ctx: " + ctx);
        NotificationReport report = new NotificationReport(txid, NotificationReport.Type.EMAIL, NotificationReport.Status.SENT);
        try {
            ConsentNotification notification = new ConsentNotification();
            notification.setLanguage(ctx.getLanguage());
            notification.setRecipient(ctx.getNotificationRecipient());
            ModelVersion notificationModel = ModelVersion.SystemHelper.findModelVersionForSerial(ConsentElementIdentifier.deserialize(ctx.getLayoutData().getNotification()).get().getSerial(), true);
            notification.setModel(notificationModel);
            if (StringUtils.isNotEmpty(ctx.getLayoutData().getTheme())) {
                ModelVersion theme = ModelVersion.SystemHelper.findModelVersionForSerial(ConsentElementIdentifier.deserialize(ctx.getLayoutData().getTheme()).get().getSerial(), true);
                notification.setTheme(theme);
            }
            AccessToken token = new AccessToken().withSubject(ctx.getSubject()).withValidity(ctx.getValidity());
            notification.setToken(this.tokenService.generateToken(token));
            if (clientConfig.isUserPageEnabled() && clientConfig.userPagePublicUrl().isPresent()) {
                ctx.setOrigin(ConsentContext.Origin.USER);
                URI notificationUri = UriBuilder.fromUri(clientConfig.userPagePublicUrl().get()).queryParam("t", notification.getToken()).build();
                notification.setUrl(notificationUri.toString());
            } else {
                ctx.setOrigin(ConsentContext.Origin.EMAIL);
                URI notificationUri = UriBuilder.fromUri(mainConfig.publicUrl()).path(ConsentsResource.class).queryParam("t", notification.getToken()).build();
                notification.setUrl(notificationUri.toString());
            }
            try {
                notification.setReceipt(this.consentService.systemRenderReceipt(txid, "application/pdf", (notification.getTheme() != null) ? notification.getTheme().entry.key : ""));
                notification.setReceiptName("receipt.pdf");
                notification.setReceiptType("application/pdf");
            } catch (ReceiptNotFoundException e) { //
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
        } catch (UnexpectedException e) {
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
