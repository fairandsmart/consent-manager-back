package com.fairandsmart.consent.template.builder;

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

import com.fairandsmart.consent.manager.ConsentForm;
import com.fairandsmart.consent.manager.model.FormLayout;
import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateModelBuilder;

import javax.enterprise.context.ApplicationScoped;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class ConsentFormTemplateModelBuilder implements TemplateModelBuilder {

    private static final Logger LOGGER = Logger.getLogger(ConsentFormTemplateModelBuilder.class.getName());

    @Override
    public boolean canBuild(Object data) {
        return data instanceof ConsentForm;
    }

    @Override
    public TemplateModel build(Object data) {
        ConsentForm form = (ConsentForm) data;
        TemplateModel<ConsentForm> model = new TemplateModel<>();
        model.setLanguage(form.getLanguage());
        ResourceBundle bundle = ResourceBundle.getBundle("freemarker/bundles/consent", Locale.forLanguageTag(model.getLanguage()));
        model.setBundle(bundle);
        model.setData(form);

        String orientation = form.getOrientation().equals(FormLayout.Orientation.HORIZONTAL) ? "horizontal" : "vertical";
        model.setTemplate("form-" + orientation + ".ftl");

        LOGGER.log(Level.FINE, model.toString());
        return model;
    }
}
