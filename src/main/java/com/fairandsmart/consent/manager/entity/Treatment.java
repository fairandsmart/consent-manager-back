package com.fairandsmart.consent.manager.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Entity
public class Treatment extends PanacheEntity implements ModelPart {

    @Id
    public String id;
    @Version
    public long lock;
    public String serial;
    public String parent;
    public String author;
    public String owner;
    public String reference;
    public String name;
    public String description;
    public String defaultLanguage;
    public String availableLanguages;
    public String country;
    public ModelPart.Status status;
    public InvalidationStrategy invalidation;
    public long creationDate;
    public long modificationDate;
    @ElementCollection
    public Set<String> translations;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getSerial() {
        return serial;
    }

    @Override
    public String getParent() {
        return parent;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public InvalidationStrategy getInvalidationStrategy() {
        return invalidation;
    }

    @Override
    public Set<String> listAvailableLanguages() {
        return Pattern.compile(",").splitAsStream(availableLanguages).collect(Collectors.toSet());
    }

    @Override
    //TODO
    public String getHash() {
        return null;
    }

}
