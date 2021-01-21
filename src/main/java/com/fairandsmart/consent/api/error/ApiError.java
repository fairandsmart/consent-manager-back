package com.fairandsmart.consent.api.error;

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

import org.apache.http.HttpStatus;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ApiError {
 
    private int status;
    private String type;
    private String title;
    private String detail;
    private String instance;
    private String stacktrace;

    public ApiError(Type type) {
        this.status = type.status;
        this.type = type.type;
        this.title = type.title;
    }

    public ApiError(Type type, String detail) {
        this(type);
        this.detail = detail;
    }

    public ApiError(Type type, String detail, String instance, String stacktrace) {
        this(type, detail);
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

    public ApiError withInstance(String instance) {
        this.setInstance(instance);
        return this;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }

    public ApiError withException(Exception exception) {
        this.setDetail(exception.getMessage());
        this.setStacktrace(Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n\t")));
        exception.printStackTrace();
        return this;
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

    public enum Type {

        ACCESS_DENIED(HttpStatus.SC_UNAUTHORIZED,"access-denied", "Access Denied"),
        INVALID_TOKEN(HttpStatus.SC_UNAUTHORIZED, "invalid-token", "Invalid Token"),
        TOKEN_EXPIRED(HttpStatus.SC_UNAUTHORIZED, "expired-token", "Expired Token"),

        ENTITY_ALREADY_EXISTS(HttpStatus.SC_CONFLICT, "already-exists", "Entity Already Exists"),

        ENTITY_NOT_FOUND(HttpStatus.SC_NOT_FOUND, "not-found", "Entity Not Found"),
        RECEIPT_NOT_FOUND(HttpStatus.SC_NOT_FOUND, "receipt-not-found", "Receipt Not Found"),
        RENDERER_NOT_FOUND(HttpStatus.SC_NOT_FOUND, "renderer-not-found", "Renderer Not Found"),

        SUPPORT_SERVICE_ERROR(HttpStatus.SC_NO_CONTENT, "support-error", "Unable to contact support service"),

        MODEL_DATA_SERIALIZATION_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR, "unexpected-error", "Unexpected Data Serialization Error"),
        RENDERER_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR, "unexpected-error", "Unexpected Renderer Error"),
        TOKEN_SERVICE_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR, "unexpected-error", "Unexpected Token Exception"),
        UNEXPECTED_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR, "unexpected-error", "Unexpected Consent Exception");

        private int status;
        private String type;
        private String title;

        Type(int status, String type, String title) {
            this.status = status;
            this.type = type;
            this.title = title;
        }

        public int getStatus() {
            return status;
        }

        public String getType() {
            return type;
        }

        public String getTitle() {
            return title;
        }
    }
}
