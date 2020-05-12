package com.fairandsmart.consent.api.error;

import com.fairandsmart.consent.api.template.TemplateBodyWriter;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

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

@Provider
@Singleton
@Produces(MediaType.TEXT_HTML)
public class ApiErrorBodyWriter implements MessageBodyWriter<ApiError> {

    private static final Logger LOGGER = Logger.getLogger(ApiErrorBodyWriter.class.getName());

    private Configuration cfg;

    public ApiErrorBodyWriter() {
        LOGGER.log(Level.INFO, "New Instance of ApiErrorBodyWriter");
    }

    @PostConstruct
    public void init() {
        LOGGER.log(Level.INFO, "Initializing ApiErrorBodyWriter");
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
    public long getSize(ApiError error, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(ApiError error, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        try {
            LOGGER.log(Level.INFO, "Applying error template to ApiError: " + error);
            Template template = cfg.getTemplate("error.ftl");
            Writer writer = new OutputStreamWriter(entityStream);
            template.process(error, writer);
        } catch ( TemplateException e ) {
            throw new WebApplicationException(e);
        }
    }
}
