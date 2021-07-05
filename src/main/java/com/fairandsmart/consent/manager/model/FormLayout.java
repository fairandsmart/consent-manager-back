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
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class FormLayout extends ModelData {

    public static final String TYPE = "layout";

    private String info;
    private List<String> elements;
    private String theme;
    private String notification;

    private Orientation orientation;
    private boolean existingElementsVisible = true;
    private String desiredReceiptMimeType;

    private boolean validityVisible = true;

    private boolean includeIFrameResizer = false;
    private boolean acceptAllVisible = false;
    private String acceptAllText;
    private boolean footerOnTop = false;

    public FormLayout() {
        this.setType(TYPE);
        this.elements = new ArrayList<>();
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public FormLayout withInfo(String info) {
        this.info = info;
        return this;
    }

    public List<String> getElements() {
        return elements;
    }

    public void setElements(List<String> elements) {
        this.elements = elements;
    }

    @Schema(hidden = true)
    public String getElementsString() {
        return String.join(",", elements);
    }

    @Schema(hidden = true)
    public void setElementsString(String elements) {
        this.setElements(Arrays.asList(elements.split(",")));
    }

    public FormLayout withElements(List<String> elements) {
        this.elements = elements;
        return this;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public FormLayout withTheme(String theme) {
        this.theme = theme;
        return this;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public FormLayout withNotification(String notification) {
        this.notification = notification;
        return this;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public FormLayout withOrientation(Orientation orientation) {
        this.orientation = orientation;
        return this;
    }

    public boolean isExistingElementsVisible() {
        return existingElementsVisible;
    }

    public void setExistingElementsVisible(boolean existingElementsVisible) {
        this.existingElementsVisible = existingElementsVisible;
    }

    public FormLayout withExistingElementsVisible(boolean existingElementsVisible) {
        this.existingElementsVisible = existingElementsVisible;
        return this;
    }

    public String getDesiredReceiptMimeType() {
        return desiredReceiptMimeType;
    }

    public void setDesiredReceiptMimeType(String desiredReceiptMimeType) {
        this.desiredReceiptMimeType = desiredReceiptMimeType;
    }

    public FormLayout withDesiredReceiptMimeType(String desiredReceiptMimeType) {
        this.desiredReceiptMimeType = desiredReceiptMimeType;
        return this;
    }

    public boolean isValidityVisible() {
        return validityVisible;
    }

    public void setValidityVisible(boolean validityVisible) {
        this.validityVisible = validityVisible;
    }

    public FormLayout withValidityVisible(boolean validityVisible) {
        this.validityVisible = validityVisible;
        return this;
    }

    public boolean isIncludeIFrameResizer() {
        return includeIFrameResizer;
    }

    public void setIncludeIFrameResizer(boolean includeIFrameResizer) {
        this.includeIFrameResizer = includeIFrameResizer;
    }

    public FormLayout withIncludeIFrameResizer(boolean includeIFrameResizer) {
        this.includeIFrameResizer = includeIFrameResizer;
        return this;
    }

    public boolean isAcceptAllVisible() {
        return acceptAllVisible;
    }

    public void setAcceptAllVisible(boolean acceptAllVisible) {
        this.acceptAllVisible = acceptAllVisible;
    }

    public FormLayout withAcceptAllVisible(boolean acceptAllVisible) {
        this.acceptAllVisible = acceptAllVisible;
        return this;
    }

    public String getAcceptAllText() {
        return acceptAllText;
    }

    public void setAcceptAllText(String acceptAllText) {
        this.acceptAllText = acceptAllText;
    }

    public FormLayout withAcceptAllText(String acceptAllText) {
        this.acceptAllText = acceptAllText;
        return this;
    }

    public boolean isFooterOnTop() {
        return footerOnTop;
    }

    public void setFooterOnTop(boolean footerOnTop) {
        this.footerOnTop = footerOnTop;
    }

    public FormLayout withFooterOnTop(boolean footerOnTop) {
        this.footerOnTop = footerOnTop;
        return this;
    }

    @Schema(hidden = true)
    public Map<String, String> getClaims() {
        Map<String, String> claims = new HashMap<>();
        if (info != null) {
            claims.put("info", this.getInfo());
        }
        if (elements != null && !elements.isEmpty()) {
            claims.put("elements", this.getElementsString());
        }
        if (theme != null) {
            claims.put("theme", this.getTheme());
        }
        if (notification != null) {
            claims.put("notification", this.getNotification());
        }

        if (orientation != null) {
            claims.put("orientation", this.getOrientation().toString());
        }
        claims.put("existingElementsVisible", Boolean.toString(this.isExistingElementsVisible()));
        if (desiredReceiptMimeType != null) {
            claims.put("desiredReceiptMimeType", this.getDesiredReceiptMimeType());
        }

        claims.put("validityVisible", Boolean.toString(this.isValidityVisible()));
        claims.put("includeIFrameResizer", Boolean.toString(this.isIncludeIFrameResizer()));
        claims.put("acceptAllVisible", Boolean.toString(this.isAcceptAllVisible()));
        if (acceptAllText != null) {
            claims.put("acceptAllText", this.getAcceptAllText());
        }
        claims.put("footerOnTop", Boolean.toString(this.isFooterOnTop()));

        return claims;
    }

    @Schema(hidden = true)
    public FormLayout setClaims(Map<String, String> claims) {
        if (claims.containsKey("info")) {
            this.setInfo(claims.get("info"));
        }
        if (claims.containsKey("elements")) {
            this.setElementsString(claims.get("elements"));
        }
        if (claims.containsKey("theme")) {
            this.setTheme(claims.get("theme"));
        }
        if (claims.containsKey("notification")) {
            this.setNotification(claims.get("notification"));
        }

        if (claims.containsKey("orientation")) {
            this.setOrientation(Orientation.valueOf(claims.get("orientation")));
        }
        if (claims.containsKey("existingElementsVisible")) {
            this.setExistingElementsVisible(Boolean.parseBoolean(claims.get("existingElementsVisible")));
        }
        if (claims.containsKey("desiredReceiptMimeType")) {
            this.setDesiredReceiptMimeType(claims.get("desiredReceiptMimeType"));
        }

        if (claims.containsKey("validityVisible")) {
            this.setValidityVisible(Boolean.parseBoolean(claims.get("validityVisible")));
        }
        if (claims.containsKey("includeIFrameResizer")) {
            this.setIncludeIFrameResizer(Boolean.parseBoolean(claims.get("includeIFrameResizer")));
        }
        if (claims.containsKey("acceptAllVisible")) {
            this.setAcceptAllVisible(Boolean.parseBoolean(claims.get("acceptAllVisible")));
        }
        if (claims.containsKey("acceptAllText")) {
            this.setAcceptAllText(claims.get("acceptAllText"));
        }
        if (claims.containsKey("footerOnTop")) {
            this.setFooterOnTop(Boolean.parseBoolean(claims.get("footerOnTop")));
        }
        return this;
    }

    @Override
    public List<Pattern> getAllowedValuesPatterns() {
        return Collections.emptyList();
    }

    @Override
    public String extractDataMimeType() {
        return MediaType.APPLICATION_JSON;
    }

    @Override
    public String toMimeContent() throws IOException {
        return this.toJson();
    }

    @Schema(description = "display layout to use", example = "VERTICAL")
    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormLayout that = (FormLayout) o;
        return existingElementsVisible == that.existingElementsVisible && validityVisible == that.validityVisible && includeIFrameResizer == that.includeIFrameResizer && acceptAllVisible == that.acceptAllVisible && footerOnTop == that.footerOnTop && Objects.equals(info, that.info) && Objects.equals(elements, that.elements) && Objects.equals(theme, that.theme) && Objects.equals(notification, that.notification) && orientation == that.orientation && Objects.equals(desiredReceiptMimeType, that.desiredReceiptMimeType) && Objects.equals(acceptAllText, that.acceptAllText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(info, elements, theme, notification, orientation, existingElementsVisible, desiredReceiptMimeType, validityVisible, includeIFrameResizer, acceptAllVisible, acceptAllText, footerOnTop);
    }

    @Override
    public String toString() {
        return "FormLayout{" +
                "info='" + info + '\'' +
                ", elements=" + elements +
                ", theme='" + theme + '\'' +
                ", notification='" + notification + '\'' +
                ", orientation=" + orientation +
                ", existingElementsVisible=" + existingElementsVisible +
                ", desiredReceiptMimeType='" + desiredReceiptMimeType + '\'' +
                ", validityVisible=" + validityVisible +
                ", includeIFrameResizer=" + includeIFrameResizer +
                ", acceptAllVisible=" + acceptAllVisible +
                ", acceptAllText='" + acceptAllText + '\'' +
                ", footerOnTop=" + footerOnTop +
                '}';
    }
}
