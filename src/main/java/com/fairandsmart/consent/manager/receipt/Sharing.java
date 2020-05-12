package com.fairandsmart.consent.manager.receipt;

import com.fairandsmart.consent.manager.data.Treatment;

public class Sharing {

    private String data;
    private Controller to;
    private Treatment.Purpose purpose;

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

    public Treatment.Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Treatment.Purpose purpose) {
        this.purpose = purpose;
    }
}
