package com.fairandsmart.consent.notification.entity;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
@Schema(name="Record", description="A Record holds a single user consent or choice")
public class NotificationReport extends PanacheEntityBase {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JsonIgnore
    @Schema(hidden = true)
    public String id;
    @Version
    @JsonIgnore
    @Schema(hidden = true)
    public long version;
    @Schema(description = "The notification transaction id", readOnly = true)
    public String transaction;
    @Schema(description = "The creation timestamp", readOnly = true)
    public long creationTimestamp;
    @Enumerated(EnumType.STRING)
    @Schema(description = "The notification status", readOnly = true)
    public Status status;
    @Enumerated(EnumType.STRING)
    @Schema(description = "The notification type", readOnly = true)
    public Type type;
    @Schema(description = "A notification explanation giving more information about what happened", readOnly = true)
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
