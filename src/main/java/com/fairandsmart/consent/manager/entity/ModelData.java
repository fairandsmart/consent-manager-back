package com.fairandsmart.consent.manager.entity;

/*-
 * #%L
 * Right Consents, a Universal Consents & Preferences Management Platform.
 * %%
 * Copyright (C) 2020 - 2021 Fair And Smart
 * %%
 * This file is part of Right Consents Community Edition.
 * 
 * Right Consents Community Edition is published by FAIR AND SMART under the
 * GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 * 
 * For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 * files, or see https://www.fairandsmart.com/opensource/.
 * #L%
 */

import com.fairandsmart.consent.manager.model.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type")
@JsonSubTypes({
        @Type(value = BasicInfo.class, name = BasicInfo.TYPE),
        @Type(value = Processing.class, name = Processing.TYPE),
        @Type(value = Conditions.class, name = Conditions.TYPE),
        @Type(value = Email.class, name = Email.TYPE),
        @Type(value = Theme.class, name = Theme.TYPE),
        @Type(value = Preference.class, name = Preference.TYPE),
        @Type(value = FormLayout.class, name = FormLayout.TYPE)
})
public abstract class ModelData {

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public abstract String extractDataMimeType();

    public abstract String toMimeContent() throws IOException;

    public abstract List<Pattern> getAllowedValuesPatterns();

    public String toJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        return mapper.writeValueAsString(this);
    }
}
