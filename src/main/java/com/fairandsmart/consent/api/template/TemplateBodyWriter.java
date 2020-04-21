package com.fairandsmart.consent.api.template;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

@Provider
@Singleton
@Produces(MediaType.TEXT_HTML)
public class TemplateBodyWriter implements MessageBodyWriter<TemplateModel> {

    private static final Logger LOGGER = Logger.getLogger(TemplateBodyWriter.class.getName());

    private Configuration cfg;

    public TemplateBodyWriter() {
        LOGGER.log(Level.INFO, "New Instance of TemplateBodyWriter");
    }

    @PostConstruct
    public void init() {
        LOGGER.log(Level.INFO, "Initializing TemplateBodyWriter");
        cfg = new Configuration();
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setClassForTemplateLoading(TemplateBodyWriter.class, "/templates");
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(TemplateModel model, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(TemplateModel model, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        try {
            LOGGER.log(Level.FINE, "Applying template to model: " + model);
            Template template = cfg.getTemplate(model.getTemplate());
            Writer writer = new OutputStreamWriter(entityStream);
            template.process(model, writer);
        } catch ( TemplateException e ) {
            throw new WebApplicationException(e);
        }
    }
}
