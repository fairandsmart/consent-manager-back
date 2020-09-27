package com.fairandsmart.consent.manager.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Subject extends PanacheEntityBase {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    public String id;
    public String owner;
    @Column(length = 2000)
    public String name;

    public static boolean existsForOwner(String owner, String name) {
        return Subject.count("owner = ?1 and  name = ?2", owner, name) > 0;
    }

}
