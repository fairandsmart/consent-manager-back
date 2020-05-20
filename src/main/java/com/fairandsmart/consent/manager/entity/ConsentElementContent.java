package com.fairandsmart.consent.manager.entity;

import com.fairandsmart.consent.manager.ModelDataSerializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.io.IOException;

@Embeddable
public class ConsentElementContent {

    @Lob
    public String data;
    public String author;
    public Class<? extends ConsentElementData> dataClass;

    public ConsentElementContent() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ConsentElementContent withData(String data) {
        this.data = data;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ConsentElementContent withAuthor(String author) {
        this.author = author;
        return this;
    }

    public ConsentElementData getDataObject() throws ModelDataSerializationException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ConsentElementData body = mapper.readValue(data, dataClass);
            return body;
        } catch ( JsonProcessingException e ) {
            throw new ModelDataSerializationException("Unable to deserialize element data", e);
        }
    }

    public void setDataObject(ConsentElementData data) throws ModelDataSerializationException {
        try {
            this.dataClass = data.getClass();
            this.data = data.toJson();
        } catch ( IOException e ) {
            throw new ModelDataSerializationException("Unable to serialize element data", e);
        }
    }

    public ConsentElementContent withDataObject(ConsentElementData data) throws ModelDataSerializationException {
        this.setDataObject(data);
        return this;
    }

    @Override
    public String toString() {
        return "ConsentElementContent{" +
                "data='" + data + '\'' +
                ", dataClass=" + dataClass +
                '}';
    }
}