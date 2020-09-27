package com.fairandsmart.consent.manager.filter;

import java.util.Map;

public interface QueryableFilter {

    String getQueryString();

    Map<String, Object> getQueryParams();

}
