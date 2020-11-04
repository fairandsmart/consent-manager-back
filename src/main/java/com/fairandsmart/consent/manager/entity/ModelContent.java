package com.fairandsmart.consent.manager.entity;

/*-
 * #%L
 * Right Consent / A Consent Manager Platform
 * 
 * Authors:
 * 
 * Xavier Lefevre <xavier.lefevre@fairandsmart.com> / FairAndSmart
 * Nicolas Rueff <nicolas.rueff@fairandsmart.com> / FairAndSmart
 * Jérôme Blanchard <jerome.blanchard@fairandsmart.com> / FairAndSmart
 * Alan Balbo <alan.balbo@fairandsmart.com> / FairAndSmart
 * Frederic Pierre <frederic.pierre@fairansmart.com> / FairAndSmart
 * Victor Guillaume <victor.guillaume@fairandsmart.com> / FairAndSmart
 * Manon Stremplewski <manon.stremplewski@fairandsmart.com> / FairAndSmart
 * Pauline Kullmann <pauline.kullmmann@fairandsmart.com> / FairAndSmart
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
