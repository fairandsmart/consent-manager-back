package com.fairandsmart.consent.api.error;

public class ApiError {
 
    private int status;
    private String type;
    private String title;
    private String detail;
    private String instance;
    private String stacktrace;

    public ApiError(int status, String type, String title) {
        this.status = status;
        this.type = type;
        this.title = title;
    }

    public ApiError(int status, String type, String title, String detail) {
        this(status, type, title);
        this.detail = detail;
    }

    public ApiError(int status, String type, String title, String detail, String instance, String stacktrace) {
        this(status, type, title, detail);
        this.instance = instance;
        this.stacktrace = stacktrace;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "status=" + status +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                ", instance='" + instance + '\'' +
                ", stacktrace='" + stacktrace + '\'' +
                '}';
    }
}