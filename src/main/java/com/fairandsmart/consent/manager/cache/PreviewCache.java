package com.fairandsmart.consent.manager.cache;

import com.fairandsmart.consent.manager.entity.ModelVersion;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class PreviewCache implements Cache<ModelVersion> {

    private final Map<String, ModelVersion> cache;

    PreviewCache() {
        cache = new HashMap<>();
    }

    @Override
    public void put(String id, ModelVersion data) {
        cache.put(id, data);
    }

    @Override
    public boolean containsKey(String id) {
        return cache.containsKey(id);
    }

    @Override
    public ModelVersion lookup(String id) {
        return cache.get(id);
    }

    @Override
    public void remove(String id) {
        cache.remove(id);
    }

}
