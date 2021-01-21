package com.fairandsmart.consent.manager.cache;

/*-
 * #%L
 * Right Consents Community Edition
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
