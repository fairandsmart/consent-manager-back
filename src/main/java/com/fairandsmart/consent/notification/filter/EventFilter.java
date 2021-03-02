package com.fairandsmart.consent.notification.filter;

import com.fairandsmart.consent.common.filter.PaginableFilter;
import com.fairandsmart.consent.common.filter.QueryableFilter;
import com.fairandsmart.consent.common.filter.SortableFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventFilter extends PaginableFilter implements SortableFilter, QueryableFilter {

    private List<String> eventTypes;
    private List<String> sourceTypes;
    private String sourceId;
    private String author;
    private int page;
    private int size;
    private String order;
    private String direction;

    public EventFilter() {
    }

    public List<String> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<String> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public EventFilter withEventTypes(List<String> eventTypes) {
        this.eventTypes =  eventTypes;
        return this;
    }

    public List<String> getSourceTypes() {
        return sourceTypes;
    }

    public void setSourceTypes(List<String> sourceTypes) {
        this.sourceTypes = sourceTypes;
    }

    public EventFilter withSourceTypes(List<String> sourceTypes) {
        this.sourceTypes =  sourceTypes;
        return this;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public EventFilter withSourceId(String sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public EventFilter withAuthor(String author) {
        this.author = author;
        return this;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public EventFilter withPage(int page) {
        this.page = page;
        return this;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public EventFilter withSize(int size) {
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

    public EventFilter withOrder(String order) {
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

    public EventFilter withDirection(String direction) {
        this.direction = direction;
        return this;
    }

    @Override
    public String getQueryString() {
        List<String> parts = new ArrayList<>();
        if (eventTypes != null && !eventTypes.isEmpty()) {
            parts.add("eventType in :eventTypes");
        }
        if (sourceTypes != null && !sourceTypes.isEmpty()) {
            parts.add("sourceType in :sourceTypes");
        }
        if (sourceId != null && !sourceId.isEmpty()) {
            parts.add("sourceId = :sourceId");
        }
        if (author != null && !author.isEmpty()) {
            parts.add("author = :author");
        }
        return String.join(" and ", parts);
    }

    @Override
    public Map<String, Object> getQueryParams() {
        Map<String, Object> params = new HashMap<>();
        if (eventTypes != null && !eventTypes.isEmpty()) {
            params.put("eventTypes", eventTypes);
        }
        if (sourceTypes != null && !sourceTypes.isEmpty()) {
            params.put("sourceTypes", sourceTypes);
        }
        if (sourceId != null && !sourceId.isEmpty()) {
            params.put("sourceId", sourceId);
        }
        if (author != null && !author.isEmpty()) {
            params.put("author", author);
        }
        return params;
    }

    @Override
    public String toString() {
        return "EventFilter{" +
                "eventTypes=" + eventTypes +
                ", sourceTypes=" + sourceTypes +
                ", sourceId='" + sourceId + '\'' +
                ", author='" + author + '\'' +
                ", page=" + page +
                ", size=" + size +
                ", order='" + order + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }
}
