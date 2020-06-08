package com.fairandsmart.consent.manager.entity;

import com.fairandsmart.consent.manager.ConsentContext;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Map;

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
    public String headSerial;
    public String bodySerial;
    public String footSerial;
    public String headKey;
    public String bodyKey;
    public String footKey;
    public String value;
    @Enumerated(EnumType.STRING)
    public Status status;
    @Enumerated(EnumType.STRING)
    public ConsentContext.CollectionMethod collectionMethod;
    public String author;
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
                "id='" + id + '\'' +
                ", version=" + version +
                ", creationTimestamp=" + creationTimestamp +
                ", expirationTimestamp=" + expirationTimestamp +
                ", owner='" + owner + '\'' +
                ", subject='" + subject + '\'' +
                ", transaction='" + transaction + '\'' +
                ", parent='" + parent + '\'' +
                ", serial='" + serial + '\'' +
                ", type='" + type + '\'' +
                ", headSerial='" + headSerial + '\'' +
                ", bodySerial='" + bodySerial + '\'' +
                ", footSerial='" + footSerial + '\'' +
                ", headKey='" + headKey + '\'' +
                ", bodyKey='" + bodyKey + '\'' +
                ", footKey='" + footKey + '\'' +
                ", value='" + value + '\'' +
                ", status=" + status +
                ", collectionMethod=" + collectionMethod +
                ", author='" + author + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}
