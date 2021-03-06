package com.fairandsmart.consent.api.dto;

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

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class CollectionPage<T> {

    private List<T> values;
    private int page;
    private int pageSize;
    private int totalPages;
    private long totalCount;
    private int limit;
    private int offset;

    public CollectionPage() {
        values = new ArrayList<>();
    }

    public CollectionPage(CollectionPage another) {
        this.page = another.page;
        this.pageSize = another.pageSize;
        this.totalPages = another.totalPages;
        this.totalCount = another.totalCount;
        this.limit = another.limit;
        this.offset = another.offset;
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
        this.offset = page * pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        this.limit = pageSize;
        this.offset = page * pageSize;
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

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    @JsonProperty("size")
    public long getSize() {
        return totalCount;
    }

    @Override
    public String toString() {
        return "CollectionPage{" +
                "values=" + values +
                ", page=" + page +
                ", pageSize=" + pageSize +
                ", totalPages=" + totalPages +
                ", totalCount=" + totalCount +
                ", limit=" + limit +
                ", offset=" + offset +
                ", size=" + totalCount +
                '}';
    }
}
