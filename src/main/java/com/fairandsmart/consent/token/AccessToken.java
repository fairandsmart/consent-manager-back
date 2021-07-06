package com.fairandsmart.consent.token;

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

import com.fairandsmart.consent.common.consts.Placeholders;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.Date;
import java.util.List;

public class AccessToken {

    private static final String DEFAULT_VALIDITY = "PT4H";
    private static final long DEFAULT_VALIDITY_MILLIS = 14400000L;

    @NotNull
    @Schema(description = "The token subject or transaction", example = Placeholders.SHELDON)
    private String subject;
    @Schema(description = "The token allowed scopes", example = Placeholders.SCOPES)
    private List<String> scopes;
    @NotNull
    @Schema(description = "The token validity (format: PnYnMnDTnHnMnS - XML Schema 1.0 section 3.2.6.1)", example = Placeholders.DURATION_2D_12H, defaultValue = "PT4H")
    private String validity = DEFAULT_VALIDITY;

    public AccessToken() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public AccessToken withSubject(String subject) {
        this.setSubject(subject);
        return this;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public AccessToken withScopes(List<String> scopes) {
        this.setScopes(scopes);
        return this;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public AccessToken withValidity(String validity) {
        this.setValidity(validity);
        return this;
    }

    public long getExpirationDelay() {
        try {
            return DatatypeFactory.newInstance().newDuration(validity).getTimeInMillis(new Date(0));
        } catch (DatatypeConfigurationException e) {
            return DEFAULT_VALIDITY_MILLIS;
        }
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "subject='" + subject + '\'' +
                ", scopes=" + scopes +
                ", validity='" + validity + '\'' +
                '}';
    }
}
