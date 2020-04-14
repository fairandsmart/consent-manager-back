package com.fairandsmart.consent.manager.filter;

import com.fairandsmart.consent.manager.entity.Information;

public class InformationFilter {

    private Information.Type type;
    private int page;
    private int size;

    public InformationFilter() {
    }

    public Information.Type getType() {
        return type;
    }

    public void setType(Information.Type type) {
        this.type = type;
    }

    public InformationFilter withType(Information.Type type) {
        this.type = type;
        return this;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public InformationFilter withPage(int page) {
        this.page = page;
        return this;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public InformationFilter withSize(int size) {
        this.size = size;
        return this;
    }

}
