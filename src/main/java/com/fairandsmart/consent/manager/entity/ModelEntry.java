package com.fairandsmart.consent.manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ModelEntry extends PanacheEntityBase {

    @JsonIgnore
    @Transient
    public static final String DEFAULT_BRANCHE = "master";

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    public String id;
    @Version
    public long version;
    public String type;
    public String key;
    public String name;
    @Column(length = 5000)
    public String description;
    public String author;
    public String owner;
    @Column(length = 5000)
    public String branches;
    public boolean hasActiveVersion;

    public static boolean isKeyAlreadyExistsForOwner(String owner, String key) {
        return ModelEntry.count("owner = ?1 and key = ?2", owner, key) > 0;
    }

    @Override
    public String toString() {
        return "ModelEntry{" +
                "id=" + id +
                ", version=" + version +
                ", type=" + type +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                ", owner='" + owner + '\'' +
                ", branches='" + branches + '\'' +
                ", hasActiveVersion=" + hasActiveVersion +
                '}';
    }

}
