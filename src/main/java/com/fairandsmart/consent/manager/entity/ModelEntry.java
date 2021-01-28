package com.fairandsmart.consent.manager.entity;

/*-
 * #%L
 * Right Consents, a Universal Consents & Preferences Management Platform.
 * %%
 * Copyright (C) 2020 - 2021 Fair And Smart
 * %%
 * This file is part of Right Consents Community Edition.
 * 
 * Right Consents Community Edition is published by FAIR AND SMART under the
 * GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 * 
 * For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 * files, or see https://www.fairandsmart.com/opensource/.
 * #L%
 */

import com.fairandsmart.consent.manager.filter.ModelFilter;
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
    @Column(length = 5000)
    public String branches;
    public long creationDate;
    public long modificationDate;
    @Enumerated(EnumType.STRING)
    public ModelFilter.Status status;
    public String defaultLanguage;
    public String availableLanguages = "";

    public static boolean isKeyAlreadyExists(String key) {
        return ModelEntry.count("key = ?1", key) > 0;
    }

    @Override
    public String toString() {
        return "ModelEntry{" +
                "id='" + id + '\'' +
                ", version=" + version +
                ", type='" + type + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                ", branches='" + branches + '\'' +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                ", status=" + status +
                ", defaultLanguage='" + defaultLanguage + '\'' +
                ", availableLanguages='" + availableLanguages + '\'' +
                '}';
    }

}
