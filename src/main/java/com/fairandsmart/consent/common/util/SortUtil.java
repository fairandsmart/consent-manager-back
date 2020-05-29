package com.fairandsmart.consent.common.util;

import com.fairandsmart.consent.manager.filter.SortableFilter;
import io.quarkus.panache.common.Sort;

public class SortUtil {

    public static Sort fromFilter(SortableFilter filter) {
        if (filter.getOrder().isEmpty()) {
            return null;
        }
        Sort.Direction direction = filter.getDirection().equalsIgnoreCase("desc") ? Sort.Direction.Descending : Sort.Direction.Ascending;
        return Sort.by(filter.getOrder(), direction);
    }
}
