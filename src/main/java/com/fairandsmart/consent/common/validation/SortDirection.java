package com.fairandsmart.consent.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static javax.validation.constraints.Pattern.Flag.CASE_INSENSITIVE;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = "^asc|desc$", flags = {CASE_INSENSITIVE})
public @interface SortDirection {
    String message() default "{invalid.direction}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
