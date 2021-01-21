package com.fairandsmart.consent.api.dto;

/*-
 * #%L
 * Right Consents Community Edition
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
