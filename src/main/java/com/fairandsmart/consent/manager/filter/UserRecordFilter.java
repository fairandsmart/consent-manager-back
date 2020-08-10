package com.fairandsmart.consent.manager.filter;

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

    public String getCollectionMethod() {
        return collectionMethod;
    }

    public void setCollectionMethod(String collectionMethod) {
        this.collectionMethod = collectionMethod;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

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
