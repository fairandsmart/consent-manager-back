package com.fairandsmart.consent.template.builder;

/*-
 * #%L
 * Right Consent / A Consent Manager Platform
 * 
 * Authors:
 * 
 * Xavier Lefevre <xavier.lefevre@fairandsmart.com> / FairAndSmart
 * Nicolas Rueff <nicolas.rueff@fairandsmart.com> / FairAndSmart
 * Jérôme Blanchard <jerome.blanchard@fairandsmart.com> / FairAndSmart
 * Alan Balbo <alan.balbo@fairandsmart.com> / FairAndSmart
 * Frederic Pierre <frederic.pierre@fairansmart.com> / FairAndSmart
 * Victor Guillaume <victor.guillaume@fairandsmart.com> / FairAndSmart
 * Manon Stremplewski <manon.stremplewski@fairandsmart.com> / FairAndSmart
 * Pauline Kullmann <pauline.kullmmann@fairandsmart.com> / FairAndSmart
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.fairandsmart.consent.api.dto.PreviewDto;
import com.fairandsmart.consent.template.TemplateModel;
import com.fairandsmart.consent.template.TemplateModelBuilder;

import javax.enterprise.context.ApplicationScoped;
import java.util.Locale;
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
        template.setLanguage(dto.getLanguage());
        ResourceBundle bundle = ResourceBundle.getBundle("freemarker/bundles/consent", Locale.forLanguageTag(template.getLanguage()));
        template.setBundle(bundle);
        template.setData(dto);
        template.setTemplate("preview/version.ftl");
        LOGGER.log(Level.FINE, template.toString());
        return template;
    }
}
