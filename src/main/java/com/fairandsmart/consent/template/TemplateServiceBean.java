package com.fairandsmart.consent.template;

/*-
 * #%L
 * Right Consents / A Consent Manager Platform
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

import com.fairandsmart.consent.api.writer.TemplateBodyWriter;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static freemarker.template.Configuration.VERSION_2_3_30;

@Singleton
public class TemplateServiceBean implements TemplateService {

    private static final Logger LOGGER = Logger.getLogger(TemplateService.class.getName());

    @Inject
    Instance<TemplateModelBuilder> builders;

    private Configuration cfg;

    @PostConstruct
    public void init() {
        LOGGER.log(Level.INFO, "Initializing TemplateBodyWriter");
        cfg = new Configuration(VERSION_2_3_30);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setClassForTemplateLoading(TemplateBodyWriter.class, "/freemarker");
    }

    @Override
    public String render(TemplateModel model) throws TemplateServiceException {
        LOGGER.log(Level.FINE, "Rendering model: " + model);
        try {
            Template template = cfg.getTemplate(model.getTemplate());
            StringWriter writer = new StringWriter();
            template.process(model, writer);
            return writer.toString();
        } catch (IOException | TemplateException e ) {
            throw new TemplateServiceException("Unable to apply template", e);
        }
    }

    @Override
    public void render(TemplateModel model, OutputStream output) throws TemplateServiceException {
        LOGGER.log(Level.FINE, "Rendering model: " + model);
        try {
            Template template = cfg.getTemplate(model.getTemplate());
            Writer writer = new OutputStreamWriter(output, StandardCharsets.UTF_8);
            template.process(model, writer);
        } catch (IOException | TemplateException e ) {
            throw new TemplateServiceException("Unable to apply template", e);
        }
    }

    @Override
    public <T> TemplateModel<T> buildModel(T data) throws TemplateServiceException {
        Optional<TemplateModel<T>> model = builders.stream().filter(b -> b.canBuild(data)).findFirst().map(b -> b.build(data));
        if (model.isPresent()) {
            return model.get();
        }
        throw new TemplateServiceException("Unable to find a builder for data of class " + data.getClass().getName());
    }

}
