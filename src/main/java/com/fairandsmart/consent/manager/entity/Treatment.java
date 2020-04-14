package com.fairandsmart.consent.manager.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Entity
public class Treatment extends PanacheEntityBase implements ModelPart {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    public UUID id;
    @Version
    public long lock;
    public String serial;
    public String parent;
    public String author;
    public String owner;
    public String reference;
    public String key;
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
    //TODO
    public String getHash() {
        return null;
    }

    @Override
    public String toString() {
        return "Treatment{" +
                "id=" + id +
                ", lock=" + lock +
                ", serial='" + serial + '\'' +
                ", parent='" + parent + '\'' +
                ", author='" + author + '\'' +
                ", owner='" + owner + '\'' +
                ", reference='" + reference + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", defaultLanguage='" + defaultLanguage + '\'' +
                ", availableLanguages='" + availableLanguages + '\'' +
                ", country='" + country + '\'' +
                ", status=" + status +
                ", invalidation=" + invalidation +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                ", translations=" + translations +
                '}';
    }
}
