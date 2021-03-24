package com.fairandsmart.consent.common.exception;

import com.fairandsmart.consent.manager.ConsentContext;

import java.util.Collections;
import java.util.Map;

public interface ReportableException {

    ConsentContext getContext();

}
