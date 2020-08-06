package com.fairandsmart.consent.notification.entity;

import java.util.HashMap;
import java.util.Map;

public class Event {
    
    public static final String AUDIT = "audit";
    public static final String SYSTEM= "system";
    public static final String SUBMIT_CONSENT = "consent.submit";

    private long timestamp;
    private String author;
    private String channel;
    private String type;
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

    public Event withAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Event withChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Event withType(String type) {
        this.type = type;
        return this;
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }

    public Event withArg(String key, String value) {
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
