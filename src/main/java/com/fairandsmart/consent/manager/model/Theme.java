package com.fairandsmart.consent.manager.model;

/*-
 * #%L
 * Right Consents, a Universal Consents & Preferences Management Platform.
 * %%
 * Copyright (C) 2020 - 2021 Fair And Smart
 * %%
 * This file is part of Right Consents Community Edition.
 * 
 * Right Consents Community Edition is published by FAIR AND SMART under the
 * GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 * 
 * For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 * files, or see https://www.fairandsmart.com/opensource/.
 * #L%
 */

import com.fairandsmart.consent.manager.entity.ModelData;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Theme extends ModelData {

    public static final String TYPE = "theme";

    private String name;
    private String icon;
    private String css;
    private String logoPath;
    private String logoAltText = "Logo";
    private LogoPosition logoPosition = LogoPosition.CENTER;

    public enum LogoPosition {
        LEFT,
        CENTER,
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
    public List<Pattern> getAllowedValuesPatterns() {
        return Collections.emptyList();
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
                Objects.equals(icon, theme.icon) &&
                Objects.equals(css, theme.css) &&
                Objects.equals(logoPath, theme.logoPath) &&
                Objects.equals(logoAltText, theme.logoAltText) &&
                Objects.equals(logoPosition, theme.logoPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, icon, css, logoPath, logoAltText, logoPosition);
    }

    @Override
    public String toString() {
        return "Theme{" +
                "name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", css='" + css + '\'' +
                ", logoPath='" + logoPath + '\'' +
                ", logoAltText='" + logoAltText + '\'' +
                ", logoPosition=" + logoPosition +
                '}';
    }
}
