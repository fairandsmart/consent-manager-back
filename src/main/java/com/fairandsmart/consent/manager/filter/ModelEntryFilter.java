package com.fairandsmart.consent.manager.filter;

import com.fairandsmart.consent.manager.entity.ModelEntry;

public class ModelEntryFilter {

    private String owner;
    private ModelEntry.Type type;
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

    public ModelEntry.Type getType() {
        return type;
    }

    public void setType(ModelEntry.Type type) {
        this.type = type;
    }

    public ModelEntryFilter withType(ModelEntry.Type type) {
        this.type = type;
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
                ", type=" + type +
                ", page=" + page +
                ", size=" + size +
                '}';
    }
}
