package com.fairandsmart.consent.manager.model;

public class Sharing {

    private String data;
    private Controller to;
    private Processing.Purpose purpose;

    public Sharing() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Controller getTo() {
        return to;
    }

    public void setTo(Controller to) {
        this.to = to;
    }

    public Processing.Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Processing.Purpose purpose) {
        this.purpose = purpose;
    }
}
