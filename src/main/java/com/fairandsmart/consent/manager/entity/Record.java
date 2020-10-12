package com.fairandsmart.consent.manager.entity;

import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.filter.SortableFilter;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Map;

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
    public String owner;
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
    public Status status;
    @Transient
    public String statusExplanation;
    @Enumerated(EnumType.STRING)
    public ConsentContext.CollectionMethod collectionMethod;
    public String author;
    @Column(length = 5000)
    public String comment;
    @ElementCollection(fetch = FetchType.EAGER)
    public Map<String, String> attributes;

    public enum Status {
        PENDING,
        COMMITTED,
        DELETED,
        VALID,
        OBSOLETE,
        EXPIRED,
        IRRELEVANT
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
                ", infoSerial='" + infoSerial + '\'' +
                ", bodySerial='" + bodySerial + '\'' +
                ", infoKey='" + infoKey + '\'' +
                ", bodyKey='" + bodyKey + '\'' +
                ", value='" + value + '\'' +
                ", status=" + status +
                ", statusExplanation='" + statusExplanation + '\'' +
                ", collectionMethod=" + collectionMethod +
                ", author='" + author + '\'' +
                ", comment='" + comment + '\'' +
                ", attributes=" + attributes +
                '}';
    }

    @Override
    public int compareTo(Record other) {
        return Long.compare(creationTimestamp, other.creationTimestamp);
    }

    public int compare(Record other, SortableFilter filter) {
        int result = 0;
        switch(filter.getOrder()) {
            case "creationTimestamp":
                result = Long.compare(creationTimestamp, other.creationTimestamp);
                break;
            case "expirationTimestamp":
                result = Long.compare(expirationTimestamp, other.expirationTimestamp);
                break;
            case "type":
                result = StringUtils.compare(type, other.type);
                break;
            case "value":
                result = StringUtils.compare(value, other.value);
                break;
            case "bodyKey":
            default:
                result = StringUtils.compare(bodyKey, other.bodyKey);
                break;
        }
        return "desc".equals(filter.getDirection()) ? -result : result;
    }

}
