package com.fairandsmart.consent.template.builder;

import com.fairandsmart.consent.manager.model.Receipt;
import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateModelBuilder;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class ReceiptTemplateModelBuilder implements TemplateModelBuilder {

    private static final Logger LOGGER = Logger.getLogger(ReceiptTemplateModelBuilder.class.getName());

    @Override
    public boolean canBuild(Object data) {
        return data instanceof Receipt;
    }

    @Override
    public TemplateModel<Receipt> build(Object data) {
        Receipt receipt = (Receipt) data;
        TemplateModel<Receipt> model = new TemplateModel<>();
        model.setLocale(LocaleUtils.toLocale(receipt.getLocale()));
        ResourceBundle bundle = ResourceBundle.getBundle("freemarker/bundles/consent", model.getLocale());
        model.setBundle(bundle);
        if (!StringUtils.isEmpty(receipt.getTransaction())) {
            model.setData(receipt);
            model.setTemplate("receipt.ftl");
        } else {
            model.setTemplate("no-receipt.ftl");
        }
        LOGGER.log(Level.FINE, model.toString());
        return model;
    }
}
