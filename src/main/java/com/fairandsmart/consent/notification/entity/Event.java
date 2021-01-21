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
