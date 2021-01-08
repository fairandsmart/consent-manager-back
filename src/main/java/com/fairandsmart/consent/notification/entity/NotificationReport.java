package com.fairandsmart.consent.notification.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

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
        SENT("SENT", "DELIVERED,OPENED,INVALID_RECIPIENT,MAILBOX_FULL"),
        DELIVERED("DELIVERED", "OPENED"),
        OPENED("OPENED", ""),
        INVALID_RECIPIENT("INVALID_RECIPIENT", ""),
        MAILBOX_FULL("MAILBOX_FULL", ""),
        ERROR("ERROR", ""),
        PENDING("PENDING", "SENT,DELIVERED,OPENED,INVALID_RECIPIENT,MAILBOX_FULL,ERROR"),
        NONE("NONE", "");

        public String name;
        public List<String> transitions;

        Status(String name, String transitions) {
            this.name = name;
            this.transitions = Arrays.asList(transitions.split(","));
        }

        public boolean isValidTransition(Status status) {
            return transitions.contains(status.name());
        }
    }

}
