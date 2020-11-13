package com.fairandsmart.consent.token.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ThinToken extends PanacheEntityBase {

    @Id
    public String id;
    @Column(length = 25000)
    public String value;
    public long expires;

}
