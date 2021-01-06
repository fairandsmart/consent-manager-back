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
    public String transaction;
    public long creationTimestamp;
    @Enumerated(EnumType.STRING)
    public Status status;
    @Enumerated(EnumType.STRING)
    public Type type;
    public String explanation;

    public NotificationReport() {
    }

    public NotificationReport(String transaction, Type type, Status status) {
        this.creationTimestamp = System.currentTimeMillis();
        this.transaction = transaction;
        this.type = type;
        this.status = status;
    }

    public enum Type {
        SMS,
        EMAIL,
        FCM,
        XMPP,
        NONE
    }

    public enum Status {
        SENT,
        DELIVERED,
        OPENED,
        INVALID_RECIPIENT,
        MAILBOX_FULL,
        ERROR,
        PENDING,
        NONE;

        public boolean canUpdate(Status status) {
            if (this == SENT) {
                return status == DELIVERED || status == OPENED || status == INVALID_RECIPIENT || status == MAILBOX_FULL;
            } else if (this == DELIVERED) {
                return status == OPENED;
            } else if (this == PENDING) {
                return status != NONE;
            } else {
                return false;
            }
        }
    }

}
