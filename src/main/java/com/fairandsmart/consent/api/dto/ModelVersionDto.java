package com.fairandsmart.consent.api.dto;

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
        for (Map.Entry<String, ModelContent> entry : version.content.entrySet()) {
            dto.data.put(entry.getKey(), entry.getValue().getDataObject());
        }
        return dto;
    }

}
