package com.fairandsmart.consent.manager.filter;

/*-
 * #%L
 * Right Consent / A Consent Manager Platform
 * 
 * Authors:
 * 
 * Xavier Lefevre <xavier.lefevre@fairandsmart.com> / FairAndSmart
 * Nicolas Rueff <nicolas.rueff@fairandsmart.com> / FairAndSmart
 * Jérôme Blanchard <jerome.blanchard@fairandsmart.com> / FairAndSmart
 * Alan Balbo <alan.balbo@fairandsmart.com> / FairAndSmart
 * Frederic Pierre <frederic.pierre@fairansmart.com> / FairAndSmart
 * Victor Guillaume <victor.guillaume@fairandsmart.com> / FairAndSmart
 * Manon Stremplewski <manon.stremplewski@fairandsmart.com> / FairAndSmart
 * Pauline Kullmann <pauline.kullmmann@fairandsmart.com> / FairAndSmart
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelFilter implements SortableFilter, PaginableFilter, QueryableFilter {

    private String owner;
    private List<String> types;
    private List<String> keys;
    private int page;
    private int size;
    private String order;
    private String direction;

    public ModelFilter() {
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public ModelFilter withTypes(List<String> types) {
        this.types = types;
        return this;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public ModelFilter withKeys(List<String> keys) {
        this.keys = keys;
        return this;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ModelFilter withPage(int page) {
        this.page = page;
        return this;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ModelFilter withSize(int size) {
        this.size = size;
        return this;
    }

    @Override
    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public ModelFilter withOrder(String order) {
        this.order = order;
        return this;
    }

    @Override
    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public ModelFilter withDirection(String direction) {
        this.direction = direction;
        return this;
    }

    @Override
    public String getQueryString() {
        List<String> parts = new ArrayList<>();
        if (owner != null && !owner.isEmpty()) {
            parts.add("owner = :owner");
        }
        if (types != null && !types.isEmpty()) {
            parts.add("type in :types");
        }
        if (keys != null && !keys.isEmpty()) {
            parts.add("key in :keys");
        }
        return String.join(" and ", parts);
    }

    @Override
    public Map<String, Object> getQueryParams() {
        Map<String, Object> params = new HashMap<>();
        if (owner != null && !owner.isEmpty()) {
            params.put("owner", owner);
        }
        if (types != null && !types.isEmpty()) {
            params.put("types", types);
        }
        if (keys != null && !keys.isEmpty()) {
            params.put("keys", keys);
        }
        return params;
    }

    @Override
    public String toString() {
        return "ModelFilter{" +
                "owner='" + owner + '\'' +
                ", types=" + types +
                ", page=" + page +
                ", size=" + size +
                ", order='" + order + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }
}
