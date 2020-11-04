package com.fairandsmart.consent.template.builder;

import com.fairandsmart.consent.api.dto.PreviewDto;
import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateModelBuilder;
import org.apache.commons.lang3.LocaleUtils;

import javax.enterprise.context.ApplicationScoped;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class ModelPreviewTemplateModelBuilder implements TemplateModelBuilder {

    private static final Logger LOGGER = Logger.getLogger(ModelPreviewTemplateModelBuilder.class.getName());

    @Override
    public boolean canBuild(Object data) {
        return data instanceof PreviewDto;
    }

    @Override
    public TemplateModel build(Object data) {
        PreviewDto dto = (PreviewDto) data;
        TemplateModel<PreviewDto> template = new TemplateModel<>();
        template.setLocale(LocaleUtils.toLocale(dto.getLanguage()));
        ResourceBundle bundle = ResourceBundle.getBundle("freemarker/bundles/consent", template.getLocale());
        template.setBundle(bundle);
        template.setData(dto);
        template.setTemplate("preview/version.ftl");
        LOGGER.log(Level.FINE, template.toString());
        return template;
    }
}
