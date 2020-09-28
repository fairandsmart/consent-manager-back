package com.fairandsmart.consent.manager.filter;

import com.fairandsmart.consent.manager.entity.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordFilter implements SortableFilter, PaginableFilter, QueryableFilter {

    private int page;
    private int size;
    private String owner;
    private String subject;
    private List<Record.Status> status;
    private List<String> headers;
    private List<String> elements;
    private List<String> footers;
    private String collectionMethod;
    private long after = -1;
    private long before = -1;
    private String value;
    private String order;
    private String direction;

    public RecordFilter() {
    }

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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<Record.Status> getStatus() {
        return status;
    }

    public void setStatus(List<Record.Status> status) {
        this.status = status;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<String> getElements() {
        return elements;
    }

    public void setElements(List<String> elements) {
        this.elements = elements;
    }

    public List<String> getFooters() {
        return footers;
    }

    public void setFooters(List<String> footers) {
        this.footers = footers;
    }

    public String getCollectionMethod() {
        return collectionMethod;
    }

    public void setCollectionMethod(String collectionMethod) {
        this.collectionMethod = collectionMethod;
    }

    public long getAfter() {
        return after;
    }

    public void setAfter(long after) {
        this.after = after;
    }

    public long getBefore() {
        return before;
    }

    public void setBefore(long before) {
        this.before = before;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
    public String getQueryString() {
        List<String> parts = new ArrayList<>();
        if (owner != null && !owner.isEmpty()) {
            parts.add("owner = :owner");
        }
        if (subject != null && !subject.isEmpty()) {
            parts.add("subject = :subject");
        }
        if (status != null && !status.isEmpty()) {
            parts.add("status in :status");
        }
        if (headers != null && !headers.isEmpty()) {
            parts.add("headSerial in :headers");
        }
        if (elements != null && elements.size() > 0) {
            parts.add("bodySerial in :elements");
        }
        if (footers != null && !footers.isEmpty()) {
            parts.add("footSerial in :footers");
        }
        if (collectionMethod != null && !collectionMethod.isEmpty()) {
            parts.add("collectionMethod = :collectionMethod");
        }
        if (before >= 0) {
            parts.add("creationTimestamp <= :before");
        }
        if (after >= 0) {
            parts.add("creationTimestamp >= :after");
        }
        if (value != null && !value.isEmpty()) {
            parts.add("value = :value");
        }
        return String.join(" and ", parts);
    }

    @Override
    public Map<String, Object> getQueryParams() {
        Map<String, Object> params = new HashMap<>();
        if (owner != null && !owner.isEmpty()) {
            params.put("owner", owner);
        }
        if (subject != null && !subject.isEmpty()) {
            params.put("subject", subject);
        }
        if (status != null && status.size() > 0) {
            params.put("status", status);
        }
        if (headers != null && headers.size() > 0) {
            params.put("headers", headers);
        }
        if (elements != null && elements.size() > 0) {
            params.put("elements", elements);
        }
        if (footers != null && footers.size() > 0) {
            params.put("footers", footers);
        }
        if (collectionMethod != null && !collectionMethod.isEmpty()) {
            params.put("collectionMethod", collectionMethod);
        }
        if (before >= 0) {
            params.put("before", before);
        }
        if (after >= 0) {
            params.put("after", after);
        }
        if (value != null && !value.isEmpty()) {
            params.put("value", value);
        }
        return params;
    }

    @Override
    public String toString() {
        return "RecordFilter{" +
                "page=" + page +
                ", size=" + size +
                ", owner='" + owner + '\'' +
                ", subject='" + subject + '\'' +
                ", status=" + status +
                ", headers=" + headers +
                ", elements=" + elements +
                ", footers=" + footers +
                ", collectionMethod='" + collectionMethod + '\'' +
                ", after=" + after +
                ", before=" + before +
                ", value='" + value + '\'' +
                ", order='" + order + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }
}
