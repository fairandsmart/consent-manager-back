package com.fairandsmart.consent.template;

import com.fairandsmart.consent.api.template.TemplateBodyWriter;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static freemarker.template.Configuration.VERSION_2_3_30;

@Singleton
public class TemplateServiceBean implements TemplateService {

    private static final Logger LOGGER = Logger.getLogger(TemplateService.class.getName());

    @Inject
    Instance<TemplateModelBuilder> builders;

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
    public <T> TemplateModel<T> buildModel(T data) throws TemplateServiceException {
        Optional<TemplateModel<T>> model = builders.stream().filter(b -> b.canBuild(data)).findFirst().map(b -> b.build(data));
        if ( !model.isPresent() ) {
            throw new TemplateServiceException("Unable to find a builder for data of class " + data.getClass().getName());
        }
        return model.get();
    }

}
