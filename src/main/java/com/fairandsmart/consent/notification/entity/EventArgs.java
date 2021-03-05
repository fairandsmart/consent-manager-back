package com.fairandsmart.consent.notification.entity;

import java.util.HashMap;

public class EventArgs extends HashMap<String, String> {

    public EventArgs() {
        super();
    }

    public EventArgs addArg(String key, String value) {
        this.put(key, value);
        return this;
    }

    public static EventArgs build(String key, String value) {
        EventArgs args = new EventArgs();
        args.put(key, value);
        return args;
    }

    public static EventArgs build() {
        return new EventArgs();
    }

}
