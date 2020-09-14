package com.fairandsmart.consent.template.builder;

import com.fairandsmart.consent.api.dto.PreviewDtoFull;
import com.fairandsmart.consent.manager.ConsentForm;
import com.fairandsmart.consent.manager.model.*;
import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateModelBuilder;
import org.apache.commons.lang3.LocaleUtils;

import javax.enterprise.context.ApplicationScoped;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class ElementPreviewTemplateModelBuilder implements TemplateModelBuilder {

    private static final Logger LOGGER = Logger.getLogger(ElementPreviewTemplateModelBuilder.class.getName());

    @Override
    public boolean canBuild(Object data) {
        return data instanceof PreviewDtoFull;
    }

    @Override
    public TemplateModel build(Object data) {
        PreviewDtoFull dto = (PreviewDtoFull) data;
        TemplateModel<ConsentForm> model = new TemplateModel<>();
        model.setLocale(LocaleUtils.toLocale(dto.getLocale()));
        ResourceBundle bundle = ResourceBundle.getBundle("freemarker/bundles/consent", model.getLocale());
        model.setBundle(bundle);

        ConsentForm form = new ConsentForm();
        form.setLocale(dto.getLocale());
        form.setOrientation(dto.getOrientation());
        form.setPreview(true);
        form.setConditions(false);
        switch (dto.getData().entry.type) {
            case Header.TYPE:
                form.setHeader(dto.getData());
                break;
            case Footer.TYPE:
                form.setFooter(dto.getData());
                break;
            case Treatment.TYPE:
                form.addElement(dto.getData());
                break;
            case Conditions.TYPE:
                form.addElement(dto.getData());
                form.setConditions(true);
                break;
            case Theme.TYPE:
                form.setTheme(dto.getData());
                break;
            case Email.TYPE:
                LOGGER.log(Level.INFO, "TODO template builder for email preview");
                break;
        }
        model.setData(form);

        if (form.isConditions()) {
            model.setTemplate("conditions.ftl");
        } else if (form.getTheme() != null) {
            model.setTemplate("preview/theme.ftl");
        } else if (form.getOrientation().equals(ConsentForm.Orientation.HORIZONTAL)) {
            model.setTemplate("preview/form-horizontal.ftl");
        } else {
            model.setTemplate("preview/form-vertical.ftl");
        }
        LOGGER.log(Level.FINE, model.toString());
        return model;
    }
}
