package com.fairandsmart.consent.manager.filter;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelFilter implements SortableFilter, PaginableFilter, QueryableFilter {

    private List<String> types;
    private List<String> keys;
    private int page;
    private int size;
    private String order;
    private String direction;

    public ModelFilter() {
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
                "types=" + types +
                ", page=" + page +
                ", size=" + size +
                ", order='" + order + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }
}
