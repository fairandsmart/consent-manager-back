package com.fairandsmart.consent.manager.model;

import com.fairandsmart.consent.manager.entity.ModelData;

import java.util.Objects;

public class Theme extends ModelData {

    public static final String TYPE = "theme";

    private String name;
    private String presentation;
    private String icon;
    private String css;
    private TargetType targetType;

    public enum TargetType {
        FORM,
        EMAIL
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

    public TargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(TargetType targetType) {
        this.targetType = targetType;
    }

    public Theme withTargetType(TargetType targetType) {
        this.targetType = targetType;
        return this;
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
                targetType == theme.targetType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, presentation, icon, css, targetType);
    }

    @Override
    public String toString() {
        return "Theme{" +
                "name='" + name + '\'' +
                ", presentation='" + presentation + '\'' +
                ", icon='" + icon + '\'' +
                ", css='" + css + '\'' +
                ", targetType=" + targetType +
                '}';
    }
}
