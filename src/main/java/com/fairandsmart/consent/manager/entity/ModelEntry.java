package com.fairandsmart.consent.manager.entity;

/*-
 * #%L
 * Right Consents Community Edition
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * You accept that the Program was not created with a view to satisfy Your
 * individual requirements. Therefore, you must ensure that the Program
 * comply with Your needs, requirements and constraints. FAIR AND SMART
 * represents and warrants that it holds, without any restriction or
 * reservation, all the legal titles, authorizations and intellectual
 * property rights granted in the context of the GPLv3 License. See the
 * Additional Terms for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <https://www.gnu.org/licenses/>.
 * 
 * You should have received a copy of the Additional Terms along with this
 * program. If not, see <https://www.fairandsmart.com/opensource/>.
 * #L%
 */

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

    public static boolean isKeyAlreadyExists(String key) {
        return ModelEntry.count("key = ?1", key) > 0;
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
                ", branches='" + branches + '\'' +
                '}';
    }

}
