package com.fairandsmart.consent.manager.model;

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

import com.fairandsmart.consent.manager.entity.ModelData;

import java.util.Objects;

public class Theme extends ModelData {

    public static final String TYPE = "theme";

    private String name;
    private String presentation;
    private String icon;
    private String css;
    private String logoPath;
    private String logoAltText = "Logo";
    private LogoPosition logoPosition = LogoPosition.MIDDLE;

    public enum LogoPosition {
        LEFT,
        MIDDLE,
        RIGHT
    }

    public Theme() {
        this.setType(TYPE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Theme withName(String name) {
        this.name = name;
        return this;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public Theme withPresentation(String presentation) {
        this.presentation = presentation;
        return this;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Theme withIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public Theme withCss(String css) {
        this.css = css;
        return this;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public Theme withLogoPath(String logoPath) {
        this.logoPath = logoPath;
        return this;
    }

    public String getLogoAltText() {
        return logoAltText;
    }

    public void setLogoAltText(String logoAltText) {
        this.logoAltText = logoAltText;
    }

    public Theme withLogoAltText(String logoAltText) {
        this.logoAltText = logoAltText;
        return this;
    }

    public LogoPosition getLogoPosition() {
        return logoPosition;
    }

    public void setLogoPosition(LogoPosition logoPosition) {
        this.logoPosition = logoPosition;
    }

    public Theme withLogoPosition(LogoPosition logoPosition) {
        this.logoPosition = logoPosition;
        return this;
    }

    @Override
    public String extractDataMimeType() {
        return "text/css";
    }

    @Override
    public String toMimeContent() {
        return this.getCss();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return Objects.equals(name, theme.name) &&
                Objects.equals(presentation, theme.presentation) &&
                Objects.equals(icon, theme.icon) &&
                Objects.equals(css, theme.css) &&
                Objects.equals(logoPath, theme.logoPath) &&
                Objects.equals(logoAltText, theme.logoAltText) &&
                Objects.equals(logoPosition, theme.logoPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, presentation, icon, css, logoPath, logoAltText, logoPosition);
    }

    @Override
    public String toString() {
        return "Theme{" +
                "name='" + name + '\'' +
                ", presentation='" + presentation + '\'' +
                ", icon='" + icon + '\'' +
                ", css='" + css + '\'' +
                ", logoPath='" + logoPath + '\'' +
                ", logoAltText='" + logoAltText + '\'' +
                ", logoPosition=" + logoPosition +
                '}';
    }
}
