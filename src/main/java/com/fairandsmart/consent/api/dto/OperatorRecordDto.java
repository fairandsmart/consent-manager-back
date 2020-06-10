package com.fairandsmart.consent.api.dto;

public class OperatorRecordDto {
    private String bodyKey;
    private String author;
    private String subject;
    private String value;
    private String comment;

    public OperatorRecordDto() {
    }

    public String getBodyKey() {
        return bodyKey;
    }

    public void setBodyKey(String bodyKey) {
        this.bodyKey = bodyKey;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
                "bodyKey='" + bodyKey + '\'' +
                ", author='" + author + '\'' +
                ", subject='" + subject + '\'' +
                ", value='" + value + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
