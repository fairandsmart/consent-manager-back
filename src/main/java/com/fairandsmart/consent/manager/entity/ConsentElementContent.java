package com.fairandsmart.consent.manager.entity;

import com.fairandsmart.consent.manager.ModelDataSerializationException;
import com.fairandsmart.consent.manager.data.ConsentElementData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.io.IOException;

@Embeddable
public class ConsentElementContent {

    @Lob
    public String data;
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

    public ConsentElementData getModelData() throws ModelDataSerializationException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ConsentElementData body = mapper.readValue(data, dataClass);
            return body;
        } catch ( JsonProcessingException e ) {
            throw new ModelDataSerializationException("Unable to deserialize element data", e);
        }
    }

    public void setModelData(ConsentElementData data) throws ModelDataSerializationException {
        try {
            this.dataClass = data.getClass();
            this.data = data.toJson();
        } catch ( IOException e ) {
            throw new ModelDataSerializationException("Unable to serialize element data", e);
        }
    }

    public ConsentElementContent withModelData(ConsentElementData data) throws ModelDataSerializationException {
        this.setModelData(data);
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