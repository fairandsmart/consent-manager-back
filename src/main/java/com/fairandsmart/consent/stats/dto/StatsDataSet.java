package com.fairandsmart.consent.stats.dto;

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

import java.util.ArrayList;
import java.util.List;

public class StatsDataSet<T> {

    private String label;
    private List<T> data;

    public StatsDataSet() {
        data = new ArrayList<>();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void addData(T value) {
        this.data.add(value);
    }

    @Override
    public String toString() {
        return "StatsDataSet{" +
                "label='" + label + '\'' +
                ", data=" + data +
                '}';
    }

}
