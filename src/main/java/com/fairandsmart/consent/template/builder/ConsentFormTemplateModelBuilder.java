package com.fairandsmart.consent.template.builder;

import com.fairandsmart.consent.manager.ConsentForm;
import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateModelBuilder;
import org.apache.commons.lang3.LocaleUtils;

import javax.enterprise.context.ApplicationScoped;
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
        model.setLocale(LocaleUtils.toLocale(form.getLocale()));
        ResourceBundle bundle = ResourceBundle.getBundle("freemarker/bundles/consent", model.getLocale());
        model.setBundle(bundle);
        model.setData(form);

        if (form.isConditions()) {
            model.setTemplate("conditions.ftl");
        } else if (form.getOrientation().equals(ConsentForm.Orientation.HORIZONTAL)) {
            model.setTemplate("form-horizontal.ftl");
        } else {
            model.setTemplate("form-vertical.ftl");
        }
        LOGGER.log(Level.FINE, model.toString());
        return model;
    }
}
