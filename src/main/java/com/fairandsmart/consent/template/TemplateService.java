package com.fairandsmart.consent.template;

import java.io.OutputStream;

public interface TemplateService {

    String render(TemplateModel model) throws TemplateServiceException;

    void render(TemplateModel model, OutputStream output) throws TemplateServiceException;

}
