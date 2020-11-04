package com.fairandsmart.consent.manager.filter;

/*-
 * #%L
 * Right Consent / A Consent Manager Plateform
 * 
 * Authors:
 * 
 * Xavier Lefevre <xavier.lefevre@fairandsmart.com> / FairAndSmart
 * Nicolas Rueff <nicolas.rueff@fairandsmart.com> / FairAndSmart
 * Jérôme Blanchard <jerome.blanchard@fairandsmart.com> / FairAndSmart
 * Alan Balbo <alan.balbo@fairandsmart.com> / FairAndSmart
 * Frederic Pierre <frederic.pierre@fairansmart.com> / FairAndSmart
 * Victor Guillaume <victor.guillaume@fairandsmart.com> / FairAndSmart
 * Manon Stremplewski <manon.stremplewski@fairandsmart.com> / FairAndSmart
 * Pauline Kullmann <pauline.kullmmann@fairandsmart.com> / FairAndSmart
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
    private Record.State state;
    private List<String> infos;
    private List<String> elements;
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

    public Record.State getState() {
        return state;
    }

    public void setState(Record.State state) {
        this.state = state;
    }

    public List<String> getInfos() {
        return infos;
    }

    public void setInfos(List<String> infos) {
        this.infos = infos;
    }

    public List<String> getElements() {
        return elements;
    }

    public void setElements(List<String> elements) {
        this.elements = elements;
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
        if (state != null) {
            parts.add("state = :state");
        }
        if (infos != null && !infos.isEmpty()) {
            parts.add("infoSerial in :infos");
        }
        if (elements != null && elements.size() > 0) {
            parts.add("bodySerial in :elements");
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
        if (state != null) {
            params.put("state", state);
        }
        if (infos != null && infos.size() > 0) {
            params.put("infos", infos);
        }
        if (elements != null && elements.size() > 0) {
            params.put("elements", elements);
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
                ", state=" + state +
                ", infos=" + infos +
                ", elements=" + elements +
                ", collectionMethod='" + collectionMethod + '\'' +
                ", after=" + after +
                ", before=" + before +
                ", value='" + value + '\'' +
                ", order='" + order + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }
}
