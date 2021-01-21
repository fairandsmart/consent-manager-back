package com.fairandsmart.consent.common.util;

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

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.manager.filter.PaginableFilter;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;

import java.util.List;

public class PageUtil {

    public static <T> CollectionPage<T> paginateQuery(PanacheQuery<T> query, PaginableFilter filter) {
        CollectionPage<T> collection = new CollectionPage<>();
        long total = query.count();
        int size = Math.max(1, filter.getSize() >= 0 ? filter.getSize() : (int) total);
        collection.setValues(query.page(Page.of(filter.getPage(), size)).list());
        collection.setPageSize(size);
        collection.setPage(filter.getPage());
        collection.setTotalPages(query.pageCount());
        collection.setTotalCount(total);
        return collection;
    }

    public static <T> CollectionPage<T> paginateList(List<T> values, PaginableFilter filter) {
        int startIndex = filter.getSize() * filter.getPage();
        int endIndex = Math.min(filter.getSize() * (filter.getPage() + 1), values.size());

        CollectionPage<T> collection = new CollectionPage<>();
        collection.setValues(values.subList(startIndex, endIndex));
        collection.setPage(filter.getPage());
        collection.setPageSize(filter.getSize());
        collection.setTotalPages((int) Math.ceil((double) (values.size()) / (double) (filter.getSize())));
        collection.setTotalCount(values.size());
        return collection;
    }
}
