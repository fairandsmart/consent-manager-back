package com.fairandsmart.consent.notification.entity;

/*-
 * #%L
 * Right Consents / A Consent Manager Platform
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
