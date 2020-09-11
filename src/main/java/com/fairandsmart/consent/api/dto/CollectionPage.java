package com.fairandsmart.consent.api.dto;

import java.util.ArrayList;
import java.util.List;

public class CollectionPage<T> {

    private List<T> values;
    private int page;
    private int pageSize;
    private int totalPages;
    private long totalCount;

    public CollectionPage() {
        values = new ArrayList<>();
    }

    public CollectionPage(CollectionPage another) {
        this.page = another.page;
        this.pageSize = another.pageSize;
        this.totalPages = another.totalPages;
        this.totalCount = another.totalCount;
        values = new ArrayList<>();
    }

    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

}
