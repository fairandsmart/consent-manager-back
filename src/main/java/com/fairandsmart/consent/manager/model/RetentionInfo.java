package com.fairandsmart.consent.manager.model;

/*-
 * #%L
 * Right Consents Community Edition
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * You accept that the Program was not created with a view to satisfy Your
 * individual requirements. Therefore, you must ensure that the Program
 * comply with Your needs, requirements and constraints. FAIR AND SMART
 * represents and warrants that it holds, without any restriction or
 * reservation, all the legal titles, authorizations and intellectual
 * property rights granted in the context of the GPLv3 License. See the
 * Additional Terms for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <https://www.gnu.org/licenses/>.
 * 
 * You should have received a copy of the Additional Terms along with this
 * program. If not, see <https://www.fairandsmart.com/opensource/>.
 * #L%
 */

public class RetentionInfo {

    public RetentionInfo() {}

    public RetentionInfo(String label, int value, RetentionUnit unit, String fullText) {
        this.label = label;
        this.value = value;
        this.unit = unit;
        this.fullText = fullText;
    }

    private String label;
    private int value;
    private RetentionUnit unit;
    private String fullText;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public RetentionUnit getUnit() {
        return unit;
    }

    public void setUnit(RetentionUnit unit) {
        this.unit = unit;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public enum RetentionUnit {
        YEAR,
        MONTH,
        WEEK
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RetentionInfo that = (RetentionInfo) o;
        return value == that.value && label.equals(that.label) && unit == that.unit && fullText.equals(that.fullText);
    }


}
