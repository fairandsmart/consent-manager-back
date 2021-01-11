package com.fairandsmart.consent.api.dto;

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
    }

}
