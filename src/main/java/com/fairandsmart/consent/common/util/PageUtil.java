package com.fairandsmart.consent.common.util;

import com.fairandsmart.consent.api.dto.CollectionPage;
import com.fairandsmart.consent.manager.filter.PaginableFilter;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;

import java.util.List;

public class PageUtil {

    public static <T> CollectionPage<T> paginateQuery(PanacheQuery<T> query, PaginableFilter filter) {
        CollectionPage<T> collection = new CollectionPage<>();
        int size = filter.getSize() >= 0 ? filter.getSize() : (int) query.count();
        collection.setValues(query.page(Page.of(filter.getPage(), size)).list());
        collection.setPageSize(size);
        collection.setPage(filter.getPage());
        collection.setTotalPages(query.pageCount());
        collection.setTotalCount(query.count());
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
