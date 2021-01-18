package com.fairandsmart.consent.api.dto;

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

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(name="Extraction Config", description="A Configuration for Record Extraction")
public class ExtractionConfigDto {

    @Schema(description = "The extraction condition" ,required = true)
    private Condition condition;

    public ExtractionConfigDto() {
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Schema(name="ExtractionConfigCondition", description="The extraction condition")
    public class Condition {

        @NotNull
        @NotEmpty
        @Schema(description = "The element key to extract", required = true)
        private String key;
        @NotNull
        @NotEmpty
        @Schema(description = "The value that need to match", required = true)
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

    @Override
    public String toString() {
        return "ExtractionConfigDto{" +
                "condition=" + condition +
                '}';
    }
}
