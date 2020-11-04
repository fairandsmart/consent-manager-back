package com.fairandsmart.consent.api.dto;

/*-
 * #%L
 * Right Consent / A Consent Manager Plateform
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
import com.fairandsmart.consent.manager.entity.ModelContent;
import com.fairandsmart.consent.manager.entity.ModelData;
import com.fairandsmart.consent.manager.entity.ModelVersion;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ModelVersionDto extends ModelVersionDtoLight {

    @NotNull
    private Map<String, ModelData> data = new HashMap<>();

    public ModelVersionDto() {
    }

    public Map<String, ModelData> getData() {
        return data;
    }

    public void setData(Map<String, ModelData> data) {
        this.data = data;
    }

    public static ModelVersionDto fromModelVersion(ModelVersion version) throws ModelDataSerializationException {
        ModelVersionDto dto = new ModelVersionDto();
        dto.setId(version.id);
        dto.setAuthor(version.author);
        dto.setParent(version.parent);
        dto.setChild(version.child);
        dto.setSerial(version.serial);
        dto.setDefaultLocale(version.defaultLocale);
        dto.setAvailableLocales(Arrays.asList(version.availableLocales.split(",")));
        dto.setStatus(version.status);
        dto.setType(version.type);
        dto.setCreationDate(version.creationDate);
        dto.setModificationDate(version.modificationDate);
        dto.setIdentifier(version.getIdentifier().toString());
        for (Map.Entry<String, ModelContent> entry : version.content.entrySet()) {
            dto.data.put(entry.getKey(), entry.getValue().getDataObject());
        }
        return dto;
    }

    @Override
    public String toString() {
        return "ModelVersionDto{" +
                "id='" + super.getId() + '\'' +
                ", serial='" + super.getSerial() + '\'' +
                ", parent='" + super.getParent() + '\'' +
                ", child='" + super.getChild() + '\'' +
                ", author='" + super.getAuthor() + '\'' +
                ", defaultLocale='" + super.getDefaultLocale() + '\'' +
                ", availableLocales=" + super.getAvailableLocales() +
                ", status=" + super.getStatus() +
                ", type=" + super.getType() +
                ", creationDate=" + super.getCreationDate() +
                ", modificationDate=" + super.getModificationDate() +
                ", identifier='" + super.getIdentifier() + '\'' +
                ", data=" + data +
                '}';
    }
}
