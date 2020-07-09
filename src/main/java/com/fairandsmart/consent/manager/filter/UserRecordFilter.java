package com.fairandsmart.consent.manager.filter;

public class UserRecordFilter implements SortableFilter {

    private int page;
    private int size;
    private String user;
    private String order;
    private String direction;

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

    @Override
    public String toString() {
        return "RecordFilter{" +
                "page=" + page +
                ", size=" + size +
                ", user='" + user + '\'' +
                ", order='" + order + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }
}
