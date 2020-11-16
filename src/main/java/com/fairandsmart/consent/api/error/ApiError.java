package com.fairandsmart.consent.api.error;

/*-
 * #%L
 * Right Consent / A Consent Manager Platform
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

import org.apache.http.HttpStatus;

import javax.ws.rs.core.Response;
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

        ENTITY_ALREADY_EXISTS(HttpStatus.SC_CONFLICT, "already-exists", "Entity Already Exists"),

        ENTITY_NOT_FOUND(HttpStatus.SC_NOT_FOUND, "not-found", "Entity Not Found"),
        RECEIPT_NOT_FOUND(HttpStatus.SC_NOT_FOUND, "receipt-not-found", "Receipt Not Found"),
        RENDERER_NOT_FOUND(HttpStatus.SC_NOT_FOUND, "renderer-not-found", "Renderer Not Found"),

        INVALID_TOKEN(HttpStatus.SC_BAD_REQUEST, "token-invalid", "Invalid Token"),
        TOKEN_EXPIRED(HttpStatus.SC_BAD_REQUEST, "token-expired", "Expired Token"),

        SUPPORT_SERVICE_ERROR(HttpStatus.SC_NO_CONTENT, "support-error", "Unable to contact support service"),
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

    }
}
