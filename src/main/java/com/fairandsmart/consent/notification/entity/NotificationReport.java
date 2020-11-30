package com.fairandsmart.consent.notification.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class NotificationReport extends PanacheEntityBase {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JsonIgnore
    public String id;
    @Version
    @JsonIgnore
    public long version;
    @JsonIgnore
    public String owner;
    public String transaction;
    public long creationTimestamp;
    @Enumerated(EnumType.STRING)
    public Status status;
    @Enumerated(EnumType.STRING)
    public Type type;
    public String explanation;

    public NotificationReport() {
    }

    public NotificationReport(String owner, String transaction, Type type, Status status) {
        this.creationTimestamp = System.currentTimeMillis();
        this.owner = owner;
        this.transaction = transaction;
        this.type = type;
        this.status = status;
    }

    public enum Type {
        SMS,
        EMAIL,
        FCM,
        XMPP
    }

    public enum Status {
        SENT,
        DELIVERED,
        OPENED,
        INVALID_RECIPIENT,
        MAILBOX_FULL,
        ERROR;
    }

}
