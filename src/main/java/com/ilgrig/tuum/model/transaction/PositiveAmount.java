package com.ilgrig.tuum.model.transaction;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PositiveAmountValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PositiveAmount {
    String message() default "Amount must be a positive number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
