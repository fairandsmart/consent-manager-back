package com.fairandsmart.consent.notification.entity;

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

public class EventType {
    
    public static final String MODEL_CREATE = "model.create";
    public static final String MODEL_UPDATE = "model.update";
    public static final String MODEL_DELETE = "model.delete";
    public static final String MODEL_VERSION_CREATE = "model.version.create";
    public static final String MODEL_VERSION_UPDATE = "model.version.update";
    public static final String MODEL_VERSION_UPDATE_STATUS = "model.version.status.update";
    public static final String MODEL_VERSION_UPDATE_TYPE = "model.version.type.update";
    public static final String MODEL_VERSION_DELETE = "model.version.delete";

    public static final String SUBJECT_CREATE = "subject.create";
    public static final String SUBJECT_UPDATE = "subject.update";
    public static final String SUBJECT_LIST_RECORDS = "subject.records.list";

    public static final String CONSENT_SUBMIT = "consent.submit";
    public static final String CONSENT_CONFIRM = "consent.confirm";

    public static final String RECEIPT_READ = "receipt.read";
    public static final String RECEIPT_STORE = "receipt.store";

    public static final String RECORD_EXTRACT = "record.extract";

    public static final String KEY_CREATE = "key.create";
    public static final String KEY_DELETE = "key.delete";

    public static final String STATS_GENERATE = "stats.generate";

}
