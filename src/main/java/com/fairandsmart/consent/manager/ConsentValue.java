package com.fairandsmart.consent.manager;

import java.util.HashMap;
import java.util.Map;

public class ConsentValue {

    private Map<String, Integer> values;

    public ConsentValue() {
        this.values = new HashMap<>();
    }

    public Map<String, Integer> getValues() {
        return values;
    }

    public void setValues(Map<String, Integer> values) {
        this.values = values;
    }

    public void putValue(String key, Integer value) {
        this.values.put(key, value);
    }
}
