package com.fairandsmart.consent.template.builder;

/*-
 * #%L
 * Right Consents Community Edition
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

import com.fairandsmart.consent.manager.ConsentFormResult;
import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateModelBuilder;

import javax.enterprise.context.ApplicationScoped;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class ConsentFormResultTemplateModelBuilder implements TemplateModelBuilder {

    private static final Logger LOGGER = Logger.getLogger(ConsentFormResultTemplateModelBuilder.class.getName());

    @Override
    public boolean canBuild(Object data) {
        return data instanceof ConsentFormResult;
    }

    @Override
    public TemplateModel<ConsentFormResult> build(Object data) {
        ConsentFormResult consentSubmissionData = (ConsentFormResult) data;

        TemplateModel<ConsentFormResult> model = new TemplateModel<>();
        model.setLanguage(consentSubmissionData.getContext().getLanguage());
        ResourceBundle bundle = ResourceBundle.getBundle("freemarker/bundles/consent", Locale.forLanguageTag(model.getLanguage()));
        model.setBundle(bundle);

        model.setData(consentSubmissionData);
        model.setTemplate("consent-form-result.ftl");

        LOGGER.log(Level.FINE, model.toString());
        return model;
    }

}

