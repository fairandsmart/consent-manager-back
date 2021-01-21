package com.fairandsmart.consent.api.dto;

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

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "A Configuration for Record Extraction")
public class ExtractionConfigDto {

    @Schema(description = "The extraction condition", required = true)
    private Condition condition;

    public ExtractionConfigDto() {
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "ExtractionConfigDto{" +
                "condition=" + condition +
                '}';
    }

    @Schema(name = "Extraction Config Condition", description = "The extraction condition")
    public class Condition {

        @NotNull
        @NotEmpty
        @Schema(description = "The element key to extract", required = true, example = "processing.001")
        private String key;
        @NotNull
        @NotEmpty
        @Schema(description = "The value that need to match", required = true, example = "accepted")
        private String value;
        @Schema(description = "is the value is a regular expression", required = true)
        private boolean regexpValue;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isRegexpValue() {
            return regexpValue;
        }

        public void setRegexpValue(boolean regexpValue) {
            this.regexpValue = regexpValue;
        }

        @Override
        public String toString() {
            return "Condition{" +
                    "key='" + key + '\'' +
                    ", value='" + value + '\'' +
                    ", regexpValue=" + regexpValue +
                    '}';
        }
    }
}
