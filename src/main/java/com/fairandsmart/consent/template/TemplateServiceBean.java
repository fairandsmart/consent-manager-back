package com.fairandsmart.consent.template;

import com.fairandsmart.consent.api.template.TemplateBodyWriter;
import com.fairandsmart.consent.manager.ConsentForm;
import com.fairandsmart.consent.manager.model.Receipt;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static freemarker.template.Configuration.VERSION_2_3_30;

@Singleton
public class TemplateServiceBean implements TemplateService {

    private static final Logger LOGGER = Logger.getLogger(TemplateService.class.getName());

    private Configuration cfg;

    @PostConstruct
    public void init() {
        LOGGER.log(Level.INFO, "Initializing TemplateBodyWriter");
        cfg = new Configuration(VERSION_2_3_30);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setClassForTemplateLoading(TemplateBodyWriter.class, "/freemarker");
    }

    @Override
    public String render(TemplateModel model) throws TemplateServiceException {
        LOGGER.log(Level.FINE, "Rendering model: " + model);
        try {
            Template template = cfg.getTemplate(model.getTemplate());
            StringWriter writer = new StringWriter();
            template.process(model, writer);
            return writer.toString();
        } catch (IOException | TemplateException e ) {
            throw new TemplateServiceException("Unable to apply template", e);
        }
    }

    @Override
    public void render(TemplateModel model, OutputStream output) throws TemplateServiceException {
        LOGGER.log(Level.FINE, "Rendering model: " + model);
        try {
            Template template = cfg.getTemplate(model.getTemplate());
            Writer writer = new OutputStreamWriter(output, Charset.forName("UTF8"));
            template.process(model, writer);
        } catch (IOException | TemplateException e ) {
            throw new TemplateServiceException("Unable to apply template", e);
        }
    }

    @Override
    public TemplateModel<ConsentForm> getFormTemplate(ConsentForm form) {
        TemplateModel<ConsentForm> model = new TemplateModel<>();
        model.setLocale(LocaleUtils.toLocale(form.getLocale()));
        ResourceBundle bundle = ResourceBundle.getBundle("freemarker/bundles/consent", model.getLocale());
        model.setBundle(bundle);
        model.setData(form);

        if (form.isConditions()) {
            model.setTemplate("conditions.ftl");
        } else if (form.getOrientation().equals(ConsentForm.Orientation.HORIZONTAL)) {
            model.setTemplate("form-horizontal.ftl");
        } else {
            model.setTemplate("form-vertical.ftl");
        }
        LOGGER.log(Level.FINE, model.toString());
        return model;
    }

    @Override
    public TemplateModel<Receipt> getReceiptTemplate(Receipt receipt) {
        TemplateModel<Receipt> model = new TemplateModel<>();
        model.setLocale(LocaleUtils.toLocale(receipt.getLocale()));
        ResourceBundle bundle = ResourceBundle.getBundle("freemarker/bundles/consent", model.getLocale());
        model.setBundle(bundle);
        if (!StringUtils.isEmpty(receipt.getTransaction())) {
            model.setData(receipt);
            model.setTemplate("receipt.ftl");
        } else {
            model.setTemplate("no-receipt.ftl");
        }
        LOGGER.log(Level.INFO, model.toString());
        return model;
    }
}
