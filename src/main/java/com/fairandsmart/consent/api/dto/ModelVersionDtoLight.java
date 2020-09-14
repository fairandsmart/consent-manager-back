package com.fairandsmart.consent.api.dto;

import com.fairandsmart.consent.manager.ModelDataSerializationException;
import com.fairandsmart.consent.manager.entity.ModelVersion;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

public class ModelVersionDtoLight {

    private String id;
    private String serial;
    private String parent;
    private String child;
    private String author;
    private String defaultLocale;
    private List<String> availableLocales;
    private ModelVersion.Status status;
    private ModelVersion.Type type;
    private long creationDate;
    private long modificationDate;

    public ModelVersionDtoLight() {
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

    public List<String> getAvailableLocales() {
        return availableLocales;
    }

    public void setAvailableLocales(List<String> availableLocales) {
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

    public static ModelVersionDtoLight fromModelVersion(ModelVersion version) throws ModelDataSerializationException {
        ModelVersionDtoLight dto = new ModelVersionDtoLight();
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
        return dto;
    }
}
