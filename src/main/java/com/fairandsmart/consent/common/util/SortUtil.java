package com.fairandsmart.consent.common.util;

import com.fairandsmart.consent.manager.filter.SortableFilter;
import io.quarkus.panache.common.Sort;
import org.apache.commons.lang3.StringUtils;

public class SortUtil {

    public static Sort fromFilter(SortableFilter filter) {
        if (StringUtils.isEmpty(filter.getOrder())) {
            return null;
        }
        Sort.Direction direction = filter.getDirection().equalsIgnoreCase("desc") ? Sort.Direction.Descending : Sort.Direction.Ascending;
        return Sort.by(filter.getOrder(), direction);
    }
}
