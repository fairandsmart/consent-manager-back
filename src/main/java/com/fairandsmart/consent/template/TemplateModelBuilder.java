package com.fairandsmart.consent.template;

public interface TemplateModelBuilder {

    boolean canBuild(Object data);

    TemplateModel build(Object data);

}
