package com.fairandsmart.consent.manager.filter;

import com.fairandsmart.consent.manager.ConsentContext;
import org.apache.commons.lang3.EnumUtils;

import java.util.Arrays;

public class UserRecordFilter implements SortableFilter {

    private int page;
    private int size;
    private String user;
    private String order;
    private String direction;
    private String collectionMethod;
    private String value;
    private Long dateAfter;
    private Long dateBefore;

    public Long getDateAfter() {
        return dateAfter;
    }

    public void setDateAfter(Long dateAfter) {
        this.dateAfter = dateAfter;
    }

    public Long getDateBefore() {
        return dateBefore;
    }

    public void setDateBefore(Long dateBefore) {
        this.dateBefore = dateBefore;
    }

    public UserRecordFilter() {}

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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCollectionMethod() {
        return collectionMethod;
    }

    public void setCollectionMethod(String collectionMethod) {
        this.collectionMethod = collectionMethod;
    }

    // Needs maintenance but prevents SQL injections
    public String getSQLOrder() {
        String sqlOrder;
        switch (order) {
            case "bodyKey":
            case "type":
            case "value":
            case "status":
            case "creationTimestamp":
            case "expirationTimestamp":
                sqlOrder = order;
                break;
            default:
                sqlOrder = "bodyKey";
        }
        switch(direction) {
            case "desc":
                sqlOrder += " DESC";
                break;
            case "asc":
            default:
                sqlOrder += " ASC";
        }
        return sqlOrder;
    }

    public String getSQLOptionalFilters() {
        String sqlFilterString = "";
        if (collectionMethod != null && EnumUtils.isValidEnum(ConsentContext.CollectionMethod.class, collectionMethod)) {
            sqlFilterString += " AND subquery.collectionMethod = '" + collectionMethod + "'";
        }
        if (Arrays.asList("accepted", "refused").contains(value)) {
            sqlFilterString += " AND subquery.value = '" + value + "'";
        }
        if (dateAfter != null && dateAfter > 0) {
            sqlFilterString += " AND subquery.creationTimestamp > " + dateAfter;
        }
        if (dateBefore != null && dateBefore > 0) {
            sqlFilterString += " AND subquery.expirationTimestamp > " + dateBefore;
        }
        return sqlFilterString;
    }

    @Override
    public String toString() {
        return "UserRecordFilter{" +
                "page=" + page +
                ", size=" + size +
                ", user='" + user + '\'' +
                ", order='" + order + '\'' +
                ", direction='" + direction + '\'' +
                ", collectionMethod='" + collectionMethod + '\'' +
                ", value='" + value + '\'' +
                ", dateAfter=" + dateAfter +
                ", dateBefore=" + dateBefore +
                '}';
    }
}
