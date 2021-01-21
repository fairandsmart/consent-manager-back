package com.fairandsmart.consent.common.util;

/*-
 * #%L
 * Right Consents / A Consent Manager Platform
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
