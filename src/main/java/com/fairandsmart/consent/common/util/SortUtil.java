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

import com.fairandsmart.consent.common.filter.SortableFilter;
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
