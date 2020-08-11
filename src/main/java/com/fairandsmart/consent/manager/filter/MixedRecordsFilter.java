package com.fairandsmart.consent.manager.filter;

import java.util.List;

public class MixedRecordsFilter implements SortableFilter, PaginableFilter {

    private int page;
    private int size;
    private List<String> users;
    private List<String> treatments;
    private List<String> conditions;
    private String order;
    private String direction;

    public MixedRecordsFilter() {}

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<String> getTreatments() {
        return treatments;
    }

    public void setTreatments(List<String> treatments) {
        this.treatments = treatments;
    }

    public List<String> getConditions() {
        return conditions;
    }

    public void setConditions(List<String> conditions) {
        this.conditions = conditions;
    }

    @Override
    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "MixedRecordsFilter{" +
                "page=" + page +
                ", size=" + size +
                ", users=" + users +
                ", treatments=" + treatments +
                ", conditions=" + conditions +
                ", order='" + order + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }
}
