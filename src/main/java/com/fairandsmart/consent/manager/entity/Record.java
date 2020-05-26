package com.fairandsmart.consent.manager.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Map;
import java.util.UUID;

@Entity
public class Record extends PanacheEntityBase {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    public String id;
    @Version
    public long version;
    public long creationTimestamp;
    public long expirationTimestamp;
    public String owner;
    public String subject;
    public String transaction;
    public String parent;
    public String serial;
    public String type;
    public String head;
    public String body;
    public String foot;
    public String value;
    @Enumerated(EnumType.STRING)
    public Status status;
    @ElementCollection(fetch = FetchType.EAGER)
    public Map<String, String> attributes;

    public enum Status {
        PENDING,
        COMMITTED,
        EXPIRED
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", version=" + version +
                ", creationTimestamp=" + creationTimestamp +
                ", expirationTimestamp=" + expirationTimestamp +
                ", owner='" + owner + '\'' +
                ", subject='" + subject + '\'' +
                ", transaction='" + transaction + '\'' +
                ", parent='" + parent + '\'' +
                ", serial='" + serial + '\'' +
                ", type='" + type + '\'' +
                ", head='" + head + '\'' +
                ", body='" + body + '\'' +
                ", foot='" + foot + '\'' +
                ", value='" + value + '\'' +
                ", status=" + status +
                ", attributes=" + attributes +
                '}';
    }
}
