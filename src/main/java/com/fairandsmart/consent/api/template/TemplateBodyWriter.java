package com.fairandsmart.consent.api.template;

import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateService;
import com.fairandsmart.consent.template.TemplateServiceException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
@Singleton
@Produces(MediaType.TEXT_HTML)
public class TemplateBodyWriter implements MessageBodyWriter<TemplateModel> {

    private static final Logger LOGGER = Logger.getLogger(TemplateBodyWriter.class.getName());

    @Inject
    TemplateService template;

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(TemplateModel model, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(TemplateModel model, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws WebApplicationException {
        try {
            LOGGER.log(Level.FINE, "Applying template to model: " + model);
            template.render(model, entityStream);
        } catch ( TemplateServiceException e ) {
            throw new WebApplicationException(e);
        }
    }
}
