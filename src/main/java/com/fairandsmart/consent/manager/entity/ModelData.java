package com.fairandsmart.consent.manager.entity;

import com.fairandsmart.consent.manager.model.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type")
@JsonSubTypes({
        @Type(value = Header.class, name = Header.TYPE),
        @Type(value = Footer.class, name = Footer.TYPE),
        @Type(value = Treatment.class, name = Treatment.TYPE),
        @Type(value = Conditions.class, name = Conditions.TYPE),
        @Type(value = Email.class, name = Email.TYPE),
        @Type(value = Theme.class, name = Theme.TYPE)
})
public abstract class ModelData {

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        return mapper.writeValueAsString(this);
    }

}
