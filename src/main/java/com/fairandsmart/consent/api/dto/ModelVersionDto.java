package com.fairandsmart.consent.api.dto;

/*-
 * #%L
 * Right Consents / A Consent Manager Platform
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * You accept that the Program was not created with a view to satisfy Your
 * individual requirements. Therefore, you must ensure that the Program
 * comply with Your needs, requirements and constraints. FAIR AND SMART
 * represents and warrants that it holds, without any restriction or
 * reservation, all the legal titles, authorizations and intellectual
 * property rights granted in the context of the GPLv3 License. See the
 * Additional Terms for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <https://www.gnu.org/licenses/>.
 * 
 * You should have received a copy of the Additional Terms along with this
 * program. If not, see <https://www.fairandsmart.com/opensource/>.
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
