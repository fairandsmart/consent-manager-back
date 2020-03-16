package com.fairandsmart.consent.serial.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;

@Entity
public class Sequence extends PanacheEntityBase {

    @Id
    public String name;
    @Version
    public long version;
    public long value;
    @Transient
    public long next;

}
