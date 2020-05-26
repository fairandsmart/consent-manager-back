package com.fairandsmart.consent.manager.filter;

public class RecordFilter {

    private int page;
    private int size;
    private String query;

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
    public String toString() {
        return "RecordFilter{" +
                "page=" + page +
                ", size=" + size +
                ", query='" + query + '\'' +
                '}';
    }
}
