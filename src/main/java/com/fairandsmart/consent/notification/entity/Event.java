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

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class Event<T>  extends PanacheEntityBase {

    public static final String NOTIFICATION_CHANNEL = "notification";
    public static final String AUDIT_CHANNEL = "audit";

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Transient
    private Set<String> channels = new HashSet<>();
    private long timestamp;
    private String author;
    private String eventType;
    private String sourceType;
    private String sourceId;
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, String> args;
    @Transient
    private T data;

    public Event() {
        args = new HashMap<>();
        timestamp = System.currentTimeMillis();
        channels.add(AUDIT_CHANNEL);
    }

    public Event(Event event) {
        this.timestamp = event.timestamp;
        this.author = event.author;
        this.eventType = event.eventType;
        this.sourceType = event.sourceType;
        this.sourceId = event.sourceId;
        this.args = event.args;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<String> getChannels() {
        return channels;
    }

    public Event<T> addChannel(String channel) {
        this.channels.add(channel);
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Event<T> withAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Event<T> withEventType(String eventType) {
        this.eventType = eventType;
        return this;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Event<T> withSourceType(String sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Event<T> withSourceId(String sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Event<T> withData(T data) {
        this.data = data;
        return this;
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }

    public Event<T> withArgs(Map<String, String> args) {
        this.args.putAll(args);
        return this;
    }

    public String getArg(String key) {
        return this.args.get(key);
    }

    public boolean hasArg(String key) {
        return this.args.containsKey(key);
    }

    public Event<T> withArg(String key, String value) {
        this.args.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        return "Event{" +
                "channels='" + channels + '\'' +
                ", id=" + id +
                ", timestamp=" + timestamp +
                ", author='" + author + '\'' +
                ", eventType='" + eventType + '\'' +
                ", sourceType='" + sourceType + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", args=" + args +
                '}';
    }
}
