package com.fairandsmart.consent.manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class ConsentElementEntry extends PanacheEntityBase {

    @JsonIgnore
    @Transient
    public static final String DEFAULT_BRANCHE = "master";

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    public UUID id;
    @Version
    public long version;
    public String type;
    public String key;
    public String name;
    public String description;
    public String owner;
    public String branches;

    public static boolean isKeyAlreadyExistsForOwner(String owner, String key) {
        return ConsentElementEntry.count("owner = ?1 and key = ?2", owner, key) > 0;
    }

    @Override
    public String toString() {
        return "ConsentElementEntry{" +
                "id=" + id +
                ", version=" + version +
                ", type=" + type +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", owner='" + owner + '\'' +
                ", branches='" + branches + '\'' +
                '}';
    }

}
