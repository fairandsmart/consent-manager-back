package com.fairandsmart.consent.common.filter;

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

public abstract class PaginableFilter {

    private int limit = 25;
    private int offset = 0;

    abstract public int getPage();

    abstract public void setPage(int page);

    abstract public int getSize();

    abstract public void setSize(int size);

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
        this.setPage((limit>0)?(offset/limit):0);
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
        this.setSize(limit);
        this.setPage((limit>0)?(offset/limit):0);
    }

}
