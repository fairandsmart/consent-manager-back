package com.fairandsmart.consent.api.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ExtractionConfigDto {

    private Condition condition;

    public ExtractionConfigDto() {
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public class Condition {

        @NotNull
        @NotEmpty
        private String key;
        @NotNull
        @NotEmpty
        private String value;
        private boolean isRegexpValue;

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
            return isRegexpValue;
        }

        public void setRegexpValue(boolean regexpValue) {
            isRegexpValue = regexpValue;
        }
    }

}
