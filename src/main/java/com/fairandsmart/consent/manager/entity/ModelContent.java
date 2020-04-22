package com.fairandsmart.consent.manager.entity;

import com.fairandsmart.consent.manager.ModelDataSerializationException;
import com.fairandsmart.consent.manager.data.ModelData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.io.IOException;

@Embeddable
public class ModelContent {

    @Lob
    public String data;
    public Class<? extends ModelData> type;

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

    public ModelData getModelData() throws ModelDataSerializationException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ModelData body = mapper.readValue(data, type);
            return body;
        } catch ( JsonProcessingException e ) {
            throw new ModelDataSerializationException("Unable to deserialize model data", e);
        }
    }

    public void setModelData(ModelData data) throws ModelDataSerializationException {
        try {
            this.type = data.getClass();
            this.data = data.toJson();
        } catch ( IOException e ) {
            throw new ModelDataSerializationException("Unable to serialize model data", e);
        }
    }

    public ModelContent withModelData(ModelData data) throws ModelDataSerializationException {
        this.setModelData(data);
        return this;
    }

    @Override
    public String toString() {
        return "ModelContent{" +
                "data='" + data + '\'' +
                ", dataType=" + type +
                '}';
    }
}