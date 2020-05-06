package com.fairandsmart.consent.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy={})
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp="^[0-9a-zA-Z-_.]{2,255}$")
public @interface ModelKey {
    String message() default "{invalid.key}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}