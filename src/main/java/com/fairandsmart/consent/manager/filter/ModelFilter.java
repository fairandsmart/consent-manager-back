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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelFilter implements SortableFilter, PaginableFilter, QueryableFilter {

    private List<String> types;
    private List<String> keys;
    private String keyword;
    private Status status;
    private String language;
    private int page;
    private int size;
    private String order;
    private String direction;

    public enum Status {
        ACTIVE,
        INACTIVE,
        INDIFFERENT
    }

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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public ModelFilter withKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ModelFilter withStatus(Status status) {
        this.status = status;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public ModelFilter withLanguage(String language) {
        this.language = language;
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
        if (keyword != null && !keyword.isEmpty()) {
            parts.add("(lower(name) like :keyword or lower(description) like :keyword)");
        }
        if (status != null && status != Status.INDIFFERENT) {
            if (status == Status.ACTIVE) {
                parts.add("status = :status");
            }
            if (status == Status.INACTIVE) {
                parts.add("status != :status");
            }
        }
        if (language != null && !language.isEmpty()) {
            parts.add(":language in availableLanguages");
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
        if (keyword != null && !keyword.isEmpty()) {
            params.put("keyword", StringUtils.lowerCase("%" + keyword + "%"));
        }
        if (status != null && status != Status.INDIFFERENT) {
            params.put("status", ModelFilter.Status.ACTIVE);
        }
        if (language != null && !language.isEmpty()) {
            params.put("language", language);
        }
        return params;
    }

    @Override
    public String toString() {
        return "ModelFilter{" +
                "types=" + types +
                ", keys=" + keys +
                ", keyword='" + keyword + '\'' +
                ", status=" + status +
                ", language='" + language + '\'' +
                ", page=" + page +
                ", size=" + size +
                ", order='" + order + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }
}
