package com.fairandsmart.consent.manager.model;

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
