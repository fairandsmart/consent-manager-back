package com.fairandsmart.consent.manager.filter;

import java.util.List;

public class ModelEntryFilter {

    private String owner;
    private List<String> types;
    private int page;
    private int size;

    public ModelEntryFilter() {
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ModelEntryFilter withOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public ModelEntryFilter withTypes(List<String> types) {
        this.types = types;
        return this;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ModelEntryFilter withPage(int page) {
        this.page = page;
        return this;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ModelEntryFilter withSize(int size) {
        this.size = size;
        return this;
    }

    @Override
    public String toString() {
        return "ModelEntryFilter{" +
                "owner='" + owner + '\'' +
                ", types=" + types +
                ", page=" + page +
                ", size=" + size +
                '}';
    }
}
