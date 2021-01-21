package com.fairandsmart.consent.manager.cache;

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
