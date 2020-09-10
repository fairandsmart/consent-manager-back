package com.fairandsmart.consent.template;

import com.fairandsmart.consent.manager.ConsentForm;
import com.fairandsmart.consent.manager.model.Receipt;

import java.io.OutputStream;

public interface TemplateService {

    String render(TemplateModel model) throws TemplateServiceException;

    void render(TemplateModel model, OutputStream output) throws TemplateServiceException;

    TemplateModel<ConsentForm> getFormTemplate(ConsentForm form);

    TemplateModel<Receipt> getReceiptTemplate(Receipt receipt);

}
