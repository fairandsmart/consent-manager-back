package com.fairandsmart.consent.api.writer;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

@Provider
@Produces("text/csv")
public class CsvBodyWriter implements MessageBodyWriter<List> {

    @Override
    public boolean isWriteable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        boolean ret = List.class.isAssignableFrom(type);
        return ret;
    }

    @Override
    public void writeTo(List data, Class type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        if (data != null && data.size() > 0 ) {
            CsvMapper mapper = new CsvMapper();
            Object first = data.get(0);
            CsvSchema schema = mapper.schemaFor(first.getClass()).withHeader();
            mapper.writer(schema).writeValue(entityStream, data);
        }
    }
}