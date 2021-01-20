package com.fairandsmart.consent.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import javax.ws.rs.core.MediaType;
import java.awt.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy={})
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = MediaType.TEXT_HTML + "|" + "application/pdf" + "|" + MediaType.TEXT_PLAIN + "|" + MediaType.APPLICATION_XML, flags = Pattern.Flag.CASE_INSENSITIVE)
public @interface ReceiptMediaType {
    String message() default "{invalid.receiptmediatype}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
