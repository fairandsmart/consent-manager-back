package com.fairandsmart.consent.manager.filter;

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
