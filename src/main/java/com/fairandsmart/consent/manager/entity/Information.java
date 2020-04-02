package com.fairandsmart.consent.manager.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Entity
public class Information extends PanacheEntityBase implements ModelPart {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    public UUID id;
    @Version
    public long lock;
    public String serial;
    public String parent;
    public Type type;
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
    @ElementCollection(fetch = FetchType.EAGER)
    public Map<String, Content> content = new HashMap<>();

    @Override
    public String getId() {
        return id.toString();
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
    public String getHash() {
        return null;
    }

    public enum Type {
        HEAD,
        FOOT
    }
}
