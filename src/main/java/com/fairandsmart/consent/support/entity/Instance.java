package com.fairandsmart.consent.support.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Instance extends PanacheEntityBase {

    @Id
    public String id;
    public String key;

}
