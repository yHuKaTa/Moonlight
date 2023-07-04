package com.aacdemy.moonlight.util.annotation;

import com.aacdemy.moonlight.util.validator.CarCategoryValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CarCategoryValidator.class})
@Documented
public @interface ValidCarTypeEnum {
    String message() default "Invalid car type!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
