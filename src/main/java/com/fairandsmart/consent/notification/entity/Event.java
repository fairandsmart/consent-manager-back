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

import java.util.HashMap;
import java.util.Map;

public class Event<T> {
    
    public static final String CONSENT_SUBMIT = "consent.submit";
    public static final String CONSENT_NOTIFY = "consent.notify";

    private long timestamp;
    private String author;
    private String type;
    private T data;
    private Map<String, String> args;

    public Event() {
        args = new HashMap<>();
        timestamp = System.currentTimeMillis();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Event<T> withType(String type) {
        this.type = type;
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
                "timestamp=" + timestamp +
                ", author='" + author + '\'' +
                ", type='" + type + '\'' +
                ", args=" + args +
                '}';
    }
}
