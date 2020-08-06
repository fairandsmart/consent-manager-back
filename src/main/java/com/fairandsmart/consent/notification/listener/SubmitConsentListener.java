package com.fairandsmart.consent.notification.listener;

import com.fairandsmart.consent.notification.entity.Event;
import com.fairandsmart.consent.template.TemplateService;
import io.quarkus.mailer.Mailer;
import io.quarkus.vertx.ConsumeEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class SubmitConsentListener {

    private static final Logger LOGGER = Logger.getLogger(SubmitConsentListener.class.getName());

    @Inject
    Mailer mailer;

    @Inject
    TemplateService template;

    @ConsumeEvent(value = Event.SUBMIT_CONSENT)
    public void consume(Event event) {
        LOGGER.log(Level.FINE, "Submit Consent event received: " + event.toString());

        Map<String, Object> data = new HashMap<>();
        //data.put("assets", assets);

        //TemplateModel<Email> model = new TemplateModel<>();
        //model.setData(data);
        //model.setLocale(new Locale(ctx.getLanguage(), ctx.getCountry()));
        //model.setBundle(bundle);


        //TODO Calculate template and send email
        //mailer.send(Mail.withHtml());
    }

    /*
    @Override
    public void handle(Event event) throws EventNotificationException {
        LOGGER.log(Level.FINE, "Handling Consent Event");
        Map<String, Object> data = new HashMap<>();
        data.put("assets", assets);
        try {
            if (event.getVolatileArgs().containsKey("ctx") && event.getVolatileArgs().containsKey("serial") ) {
                ConsentContext ctx = ConsentContext.fromJson(event.getVolatileArgs().get("ctx"));

                if (ctx.getOptoutEmail() != null && !ctx.getOptoutEmail().isEmpty()) {

                    String serial = event.getVolatileArgs().get("serial");
                    String optoutEmail = ctx.getOptoutEmail();
                    ctx.setOptoutEmail(null);

                    if ( event.getVolatileArgs().containsKey("receipt") ) {
                        ConsentReceipt receipt = ConsentReceipt.fromJson(event.getVolatileArgs().get("receipt"));
                        data.put("receipt", receipt);
                    }

                    LOGGER.log(Level.FINE, "Consent serial:" + serial);
                    ConsentModel consentModel = consentService.findModelForConsentSerial(serial);
                    LOGGER.log(Level.FINE, "Consent model loaded: " + consentModel);
                    String nextserial = consentService.systemGenerateConsentSerial(consentModel.getId());
                    LOGGER.log(Level.FINE, "Next consent serial generated: " + nextserial);
                    ctx.setPreviousSerial(serial);
                    ResourceBundle bundle = ResourceBundle.getBundle("templates/bundles/consent", new Locale(ctx.getLanguage(), ctx.getCountry()));
                    String token = tokenService.generateToken(nextserial, Collections.singletonMap("ctx", ctx.toJson()), Calendar.DAY_OF_MONTH, 14);
                    URI optoutUri = UriBuilder.fromUri(publicUrl).path("api").path(ConsentResource.class).path(nextserial).queryParam("t", token).build();

                    data.put("instance", instance);
                    data.put("ctx", ctx);
                    data.put("model", consentModel);
                    data.put("optoutUrl", optoutUri.toString());

                    ConsentOptOutModel optOutModel;
                    if (consentModel.getOptOutModel() == null || consentModel.getOptOutModel().isEmpty()) {
                        throw new ConsentModelOptOutModelNotSetException();
                    } else {
                        EntityIdentifier optOutModelIdentifier = EntityIdentifier.deserialize(consentModel.getOptOutModel());
                        optOutModel = consentService.getOptOutModel(optOutModelIdentifier.getEntityId());
                    }
                    data.put("optOutModel", optOutModel);

                    TemplateModel<Map> model = new TemplateModel<>();
                    model.setData(data);
                    model.setLocale(new Locale(ctx.getLanguage(), ctx.getCountry()));
                    model.setBundle(bundle);

                    String from = optOutModel.getSender().getOrDefault(ctx.getLanguage(),
                            optOutModel.getSender().size() > 0 ? optOutModel.getSender().values().iterator().next() : fromRecipientFs);
                    String subject = optOutModel.getSubject().getOrDefault(ctx.getLanguage(),
                            optOutModel.getSubject().size() > 0 ? optOutModel.getSubject().values().iterator().next() : "");
                    String body = templateService.render("consent/consent-opt-out-email-body", model.getLocale(), model);

                    transportService.sendSmtp(from, optoutEmail, noreplyRecipientFs, subject, body, "text/html; charset=\"UTF-8\"");
                } else {
                    LOGGER.log(Level.INFO, "no optout email set in attributes, nothing to send");
                }
            } else {
                LOGGER.log(Level.INFO, "ctx or serial is missing in event, nothing to notify...");
            }

        } catch (EntityNotFoundException | RenderingException | TransportServiceException | IOException | CoreSecurityException e) {
            throw new EventNotificationException("Unable to handle consent submission event for mail optout", e);
        }
    }
     */

}
