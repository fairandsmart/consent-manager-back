package com.fairandsmart.consent.template.builder;

import com.fairandsmart.consent.api.resource.ReceiptsResource;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.model.Receipt;
import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateModelBuilder;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import java.net.URI;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class ConsentResponseTemplateModelBuilder implements TemplateModelBuilder {

    private static final Logger LOGGER = Logger.getLogger(ConsentResponseTemplateModelBuilder.class.getName());

    @Override
    public boolean canBuild(Object data) {
        return data instanceof ConsentResponseData;
    }

    @Override
    public TemplateModel<ConsentResponseData> build(Object data) {
        ConsentResponseData consentResponseData = (ConsentResponseData) data;

        TemplateModel<ConsentResponseData> model = new TemplateModel<>();
        model.setLocale(LocaleUtils.toLocale(consentResponseData.getContext().getLocale()));
        ResourceBundle bundle = ResourceBundle.getBundle("freemarker/bundles/consent", model.getLocale());
        model.setBundle(bundle);

        model.setData(consentResponseData);
        model.setTemplate("consent-response.ftl");
        return model;
    }

    public static class ConsentResponseData {
        ConsentContext context;
        URI receiptURI;

        public void setContext(ConsentContext context) {
            this.context = context;
        }

        public ConsentContext getContext() {
            return context;
        }

        public void setReceiptURI(URI receiptURI) {
            this.receiptURI = receiptURI;
        }

        public URI getReceiptURI() {
            return receiptURI;
        }

        @Override
        public String toString() {
            return "ConsentResponseData{" +
                    "context=" + context +
                    ", receiptURI=" + receiptURI +
                    '}';
        }
    }
}