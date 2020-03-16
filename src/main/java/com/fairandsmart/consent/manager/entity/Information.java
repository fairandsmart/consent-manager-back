package com.fairandsmart.consent.manager.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.Map;
import java.util.Set;

@Entity
public class Information extends PanacheEntity implements ModelPart {

    @Id
    public String id;
    @Version
    public long lock;
    public String serial;
    public String version;
    private String author;
    private String owner;
    private String reference;
    public String name;
    public String description;
    public String country;
    //private ModelPart.Status status;
    public String parent;
    public boolean invalidateAncestors;
    private long creationDate;
    private long modificationDate;
    @ElementCollection
    public Set<String> availableLanguages;
    @ElementCollection
    public Map<String, Content> content;


    //TODO attachments
    //public List<String> attachments


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getSerial() {
        return serial;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public boolean isInvalidateAncestors() {
        return false;
    }

    @Override
    public String getCountry() {
        return null;
    }

    @Override
    public Set<String> getAvailableLanguages() {
        return null;
    }

    @Override
    public String hash() {
        return null;
    }
}
