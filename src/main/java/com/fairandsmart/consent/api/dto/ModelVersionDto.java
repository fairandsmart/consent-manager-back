package com.fairandsmart.consent.api.dto;

import com.fairandsmart.consent.manager.ModelDataSerializationException;
import com.fairandsmart.consent.manager.entity.ModelContent;
import com.fairandsmart.consent.manager.entity.ModelData;
import com.fairandsmart.consent.manager.entity.ModelVersion;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ModelVersionDto {

    private String id;
    private String serial;
    private String parent;
    private String child;
    private String author;
    @NotNull
    private String defaultLocale;
    private String[] availableLocales;
    private ModelVersion.Status status;
    private ModelVersion.Type type;
    private long creationDate;
    private long modificationDate;
    @NotNull
    private Map<String, ModelData> data = new HashMap<>();

    public ModelVersionDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDefaultLocale() {
        return defaultLocale;
    }

    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public String[] getAvailableLocales() {
        return availableLocales;
    }

    public void setAvailableLocales(String[] availableLocales) {
        this.availableLocales = availableLocales;
    }

    public ModelVersion.Status getStatus() {
        return status;
    }

    public void setStatus(ModelVersion.Status status) {
        this.status = status;
    }

    public ModelVersion.Type getType() {
        return type;
    }

    public void setType(ModelVersion.Type type) {
        this.type = type;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public long getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(long modificationDate) {
        this.modificationDate = modificationDate;
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
        dto.setAvailableLocales(version.availableLocales.split(","));
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
