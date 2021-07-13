package com.fairandsmart.consent.manager.filter;

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

import com.fairandsmart.consent.common.filter.QueryableFilter;
import com.fairandsmart.consent.common.filter.SortableFilter;
import com.fairandsmart.consent.manager.entity.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordFilter implements SortableFilter, QueryableFilter {

    private String subject;
    private List<Record.State> states;
    private List<String> infos;
    private List<String> elements;
    private List<String> origins;
    private long after = -1;
    private long before = -1;
    private String value;
    private String order;
    private String direction;

    public RecordFilter() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public RecordFilter withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public List<Record.State> getStates() {
        return states;
    }

    public void setStates(List<Record.State> states) {
        this.states = states;
    }

    public RecordFilter withStates(List<Record.State> states) {
        this.states = states;
        return this;
    }

    public List<String> getInfos() {
        return infos;
    }

    public void setInfos(List<String> infos) {
        this.infos = infos;
    }

    public RecordFilter withInfos(List<String> infos) {
        this.infos = infos;
        return this;
    }

    public List<String> getElements() {
        return elements;
    }

    public void setElements(List<String> elements) {
        this.elements = elements;
    }

    public RecordFilter withElements(List<String> elements) {
        this.elements = elements;
        return this;
    }

    public List<String> getOrigins() {
        return origins;
    }

    public void setOrigins(List<String> origins) {
        this.origins = origins;
    }

    public RecordFilter withOrigins(List<String> origins) {
        this.origins = origins;
        return this;
    }

    public long getAfter() {
        return after;
    }

    public void setAfter(long after) {
        this.after = after;
    }

    public RecordFilter withAfter(long after) {
        this.after = after;
        return this;
    }

    public long getBefore() {
        return before;
    }

    public void setBefore(long before) {
        this.before = before;
    }

    public RecordFilter withBefore(long before) {
        this.before = before;
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public RecordFilter withValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public RecordFilter withOrder(String order) {
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

    public RecordFilter withDirection(String direction) {
        this.direction = direction;
        return this;
    }

    @Override
    public String getQueryString() {
        List<String> parts = new ArrayList<>();
        if (subject != null && !subject.isEmpty()) {
            parts.add("subject = :subject");
        }
        if (states != null && !states.isEmpty()) {
            parts.add("state in :states");
        }
        if (infos != null && !infos.isEmpty()) {
            parts.add("infoSerial in :infos");
            parts.add("infoSerial != ''");
        }
        if (elements != null && !elements.isEmpty()) {
            parts.add("bodySerial in :elements");
        }
        if (origins != null && !origins.isEmpty()) {
            parts.add("origin in :origins");
        }
        if (before > 0) {
            parts.add("creationTimestamp <= :before");
        }
        if (after > 0) {
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
        if (subject != null && !subject.isEmpty()) {
            params.put("subject", subject);
        }
        if (states != null && !states.isEmpty()) {
            params.put("states", states);
        }
        if (infos != null && !infos.isEmpty()) {
            params.put("infos", infos);
        }
        if (elements != null && !elements.isEmpty()) {
            params.put("elements", elements);
        }
        if (origins != null && !origins.isEmpty()) {
            params.put("origins", origins);
        }
        if (before > 0) {
            params.put("before", before);
        }
        if (after > 0) {
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
                "subject='" + subject + '\'' +
                ", states=" + states +
                ", infos=" + infos +
                ", elements=" + elements +
                ", origins=" + origins +
                ", after=" + after +
                ", before=" + before +
                ", value='" + value + '\'' +
                ", order='" + order + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }
}
