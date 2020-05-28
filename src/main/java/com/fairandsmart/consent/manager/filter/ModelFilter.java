package com.fairandsmart.consent.manager.filter;

import java.util.List;

public class ModelFilter {

    private List<String> types;
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
