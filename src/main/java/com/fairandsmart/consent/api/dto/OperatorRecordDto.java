package com.fairandsmart.consent.api.dto;

import java.util.Map;

public class OperatorRecordDto {
    private String token;
    private Map<String, String> values;
    private String comment;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "OperatorRecordDto{" +
                "token='" + token + '\'' +
                ", values=" + values +
                ", comment='" + comment + '\'' +
                '}';
    }
}
