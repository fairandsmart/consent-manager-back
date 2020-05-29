package com.fairandsmart.consent.manager.filter;

public class RecordFilter implements SortableFilter {

    private int page;
    private int size;
    private String query;
    private String order;
    private String direction;

    public RecordFilter() {}

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

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
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
        return "RecordFilter{" +
                "page=" + page +
                ", size=" + size +
                ", query='" + query + '\'' +
                ", order='" + order + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }
}
