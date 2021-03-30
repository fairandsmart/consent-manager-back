package com.fairandsmart.consent.manager.entity;

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

import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.ConsentElementIdentifier;
import com.fairandsmart.consent.manager.model.Conditions;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Entity
public class Record extends PanacheEntityBase implements Comparable<Record> {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    public String id;
    @Version
    public long version;
    public long creationTimestamp;
    public long expirationTimestamp;
    @Column(length = 2000)
    public String subject;
    public String transaction;
    public String parent;
    public String serial;
    public String type;
    public String infoSerial;
    public String bodySerial;
    public String infoKey;
    public String bodyKey;
    public String value;
    @Enumerated(EnumType.STRING)
    public State state;
    public String origin;
    public String author;
    @Column(length = 5000)
    public String comment;
    @ElementCollection(fetch = FetchType.EAGER)
    public Map<String, String> attributes;
    @Transient
    public Status status;
    @Transient
    public StatusExplanation statusExplanation;

    public enum State {
        UNKNOWN,
        PENDING,
        COMMITTED,
        DELETED
    }

    public enum Status {
        VALID,
        OBSOLETE,
        EXPIRED,
        IRRELEVANT,
        UNKNOWN
    }

    public enum StatusExplanation {
        LATEST_VALID,
        OBSOLETE,
        EXPIRED,
        INFO_SERIAL_ARCHIVED,
        BODY_SERIAL_ARCHIVED,
        STILL_PENDING,
        ENTRY_DELETED
    }

    public static Record build(ConsentContext ctx, String transaction, String defaultAuthor, Instant now, ConsentElementIdentifier info, ConsentElementIdentifier body, String value, String comment) {
        Record record = new Record();
        record.transaction = transaction;
        record.subject = ctx.getSubject();
        record.type = body.getType();
        record.infoSerial = info.getSerial();
        record.bodySerial = body.getSerial();
        record.infoKey = info.getKey();
        record.bodyKey = body.getKey();
        record.serial = (record.infoSerial.isEmpty() ? "" : record.infoSerial + ".") + record.bodySerial;
        record.value = value;
        record.creationTimestamp = now.toEpochMilli();
        record.expirationTimestamp = Conditions.TYPE.equals(record.type) ? 0 : now.plusMillis(ctx.getValidityInMillis()).toEpochMilli();
        record.state = Record.State.COMMITTED;
        record.origin = ctx.getOrigin();
        record.author = StringUtils.isNotEmpty(ctx.getAuthor()) ? ctx.getAuthor() : defaultAuthor;
        record.comment = comment;
        return record;
    }

    public static long countRecordTransactions(long from, long to){
        return Record.find("select distinct r.transaction from Record r where r.creationTimestamp > ?1 and r.creationTimestamp < ?2", from, to).count();
    }

    public static Record.State findTransactionState(String transaction){
        //We only get the first because all records state should be identical in the same transaction
        Optional<Record> result = Record.find("transaction = ?1", transaction).firstResultOptional();
        if (result.isPresent()) {
            return result.get().state;
        } else {
            return State.UNKNOWN;
        }
    }

    public static boolean isTransactionExists(String transaction) {
        Optional<Record> result = Record.find("transaction = ?1", transaction).firstResultOptional();
        return result.isPresent();
    }

    @Override
    public String toString() {
        return "Record{" +
                "id='" + id + '\'' +
                ", version=" + version +
                ", creationTimestamp=" + creationTimestamp +
                ", expirationTimestamp=" + expirationTimestamp +
                ", subject='" + subject + '\'' +
                ", transaction='" + transaction + '\'' +
                ", parent='" + parent + '\'' +
                ", serial='" + serial + '\'' +
                ", type='" + type + '\'' +
                ", infoSerial='" + infoSerial + '\'' +
                ", bodySerial='" + bodySerial + '\'' +
                ", infoKey='" + infoKey + '\'' +
                ", bodyKey='" + bodyKey + '\'' +
                ", value='" + value + '\'' +
                ", state=" + state +
                ", status=" + status +
                ", statusExplanation=" + statusExplanation +
                ", origin=" + origin +
                ", author='" + author + '\'' +
                ", comment='" + comment + '\'' +
                ", attributes=" + attributes +
                '}';
    }

    @Override
    public int compareTo(Record other) {
        return Long.compare(creationTimestamp, other.creationTimestamp);
    }

}
