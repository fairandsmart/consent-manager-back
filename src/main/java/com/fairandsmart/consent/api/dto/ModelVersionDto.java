package com.fairandsmart.consent.api.dto;

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

import com.fairandsmart.consent.manager.entity.ModelContent;
import com.fairandsmart.consent.manager.entity.ModelData;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.exception.ModelDataSerializationException;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ModelVersionDto extends ModelVersionDtoLight {

    @NotNull
    private Map<String, ModelData> data = new HashMap<>();

    public ModelVersionDto() {
    }

    public static ModelVersionDto fromModelVersion(ModelVersion version) throws ModelDataSerializationException {
        ModelVersionDto dto = new ModelVersionDto();
        dto.setId(version.id);
        dto.setAuthor(version.author);
        dto.setParent(version.parent);
        dto.setChild(version.child);
        dto.setSerial(version.serial);
        dto.setDefaultLanguage(version.defaultLanguage);
        dto.setAvailableLanguages(Arrays.asList(version.availableLanguages.split(",")));
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

    public Map<String, ModelData> getData() {
        return data;
    }

    public void setData(Map<String, ModelData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ModelVersionDto{" +
                "id='" + super.getId() + '\'' +
                ", serial='" + super.getSerial() + '\'' +
                ", parent='" + super.getParent() + '\'' +
                ", child='" + super.getChild() + '\'' +
                ", author='" + super.getAuthor() + '\'' +
                ", defaultLanguage='" + super.getDefaultLanguage() + '\'' +
                ", availableLanguages=" + super.getAvailableLanguages() +
                ", status=" + super.getStatus() +
                ", type=" + super.getType() +
                ", creationDate=" + super.getCreationDate() +
                ", modificationDate=" + super.getModificationDate() +
                ", identifier='" + super.getIdentifier() + '\'' +
                ", data=" + data +
                '}';
    }
}
