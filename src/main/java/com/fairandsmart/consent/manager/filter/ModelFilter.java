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

import com.fairandsmart.consent.common.filter.PaginableFilter;
import com.fairandsmart.consent.common.filter.QueryableFilter;
import com.fairandsmart.consent.common.filter.SortableFilter;
import com.fairandsmart.consent.manager.entity.ModelEntry;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelFilter extends PaginableFilter implements SortableFilter, QueryableFilter {

    private List<String> types;
    private List<String> keys;
    private String keyword;
    private List<ModelEntry.Status> statuses;
    private List<String> languages;
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

    public List<ModelEntry.Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<ModelEntry.Status> statuses) {
        this.statuses = statuses;
    }

    public ModelFilter withStatuses(List<ModelEntry.Status> statuses) {
        this.statuses = statuses;
        return this;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public ModelFilter withLanguages(List<String> languages) {
        this.languages = languages;
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
        if (statuses != null && !statuses.isEmpty()) {
            parts.add("status in :statuses");
        }
        if (languages != null && !languages.isEmpty()) {
            List<String> languagesParts = new ArrayList<>();
            for (int index = 0; index < languages.size(); index++) {
                if (languages.get(index).isEmpty()) {
                    languagesParts.add("availableLanguages = '' or availableLanguages is null");
                } else {
                    languagesParts.add("availableLanguages like concat('%',concat(:language" + index + ",'%'))");
                }
            }
            parts.add("(" + String.join(" or ", languagesParts) + ")");
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
        if (statuses != null && !statuses.isEmpty()) {
            params.put("statuses", statuses);
        }
        if (languages != null && !languages.isEmpty()) {
            for (int index = 0; index < languages.size(); index++) {
                if (!languages.get(index).isEmpty()) {
                    params.put("language" + index, languages.get(index));
                }
            }
        }
        return params;
    }

    @Override
    public String toString() {
        return "ModelFilter{" +
                "types=" + types +
                ", keys=" + keys +
                ", keyword='" + keyword + '\'' +
                ", statuses=" + statuses +
                ", languages=" + languages +
                ", page=" + page +
                ", size=" + size +
                ", order='" + order + '\'' +
                ", direction='" + direction + '\'' +
                ", limit=" + getLimit() +
                ", offset=" + getOffset() +
                '}';
    }
}
