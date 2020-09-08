package com.fairandsmart.consent.manager.entity;

import com.fairandsmart.consent.manager.ModelDataSerializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.io.IOException;

@Embeddable
public class ModelContent {

    @Lob
    public String data;
    public String author;
    @Column(length = 2000)
    public Class<? extends ModelData> dataClass;

    public ModelContent() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ModelContent withData(String data) {
        this.data = data;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ModelContent withAuthor(String author) {
        this.author = author;
        return this;
    }

    public ModelData getDataObject() throws ModelDataSerializationException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, dataClass);
        } catch ( JsonProcessingException e ) {
            throw new ModelDataSerializationException("Unable to deserialize element data", e);
        }
    }

    public void setDataObject(ModelData data) throws ModelDataSerializationException {
        try {
            this.dataClass = data.getClass();
            this.data = data.toJson();
        } catch ( IOException e ) {
            throw new ModelDataSerializationException("Unable to serialize element data", e);
        }
    }

    public ModelContent withDataObject(ModelData data) throws ModelDataSerializationException {
        this.setDataObject(data);
        return this;
    }

    @Override
    public String toString() {
        return "ModelContent{" +
                "data='" + data + '\'' +
                ", dataClass=" + dataClass +
                '}';
    }
}