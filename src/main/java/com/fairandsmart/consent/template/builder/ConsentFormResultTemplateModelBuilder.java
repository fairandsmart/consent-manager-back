package com.fairandsmart.consent.template.builder;

import com.fairandsmart.consent.manager.ConsentFormResult;
import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateModelBuilder;
import org.apache.commons.lang3.LocaleUtils;

import javax.enterprise.context.ApplicationScoped;
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
        model.setLocale(LocaleUtils.toLocale(consentSubmissionData.getContext().getLocale()));
        ResourceBundle bundle = ResourceBundle.getBundle("freemarker/bundles/consent", model.getLocale());
        model.setBundle(bundle);

        model.setData(consentSubmissionData);
        model.setTemplate("consent-form-result.ftl");

        LOGGER.log(Level.FINE, model.toString());
        return model;
    }

}

