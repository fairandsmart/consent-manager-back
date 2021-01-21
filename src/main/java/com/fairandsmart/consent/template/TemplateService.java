package com.fairandsmart.consent.template;

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

import java.io.OutputStream;

public interface TemplateService {

    String render(TemplateModel model) throws TemplateServiceException;

    void render(TemplateModel model, OutputStream output) throws TemplateServiceException;

    <T> TemplateModel<T> buildModel(T data) throws TemplateServiceException;

}
