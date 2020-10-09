package com.fairandsmart.consent.security.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AccessLog extends PanacheEntityBase {

    @Id
    public String username;
    public long timestamp;

    @Override
    public String toString() {
        return "AccessLog{" +
                "username='" + username + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
