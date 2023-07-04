package com.aacdemy.moonlight.util.validator;

import com.aacdemy.moonlight.util.annotation.UppercaseEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.Arrays;

@Configurable

public class UppercaseEnumValidator implements ConstraintValidator<UppercaseEnum, String> {
    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(UppercaseEnum constraintAnnotation) {
        enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String uppercaseValue = value.toUpperCase();

        Enum<?>[] enumValues = enumClass.getEnumConstants();
        return Arrays.stream(enumValues)
                .map(Enum::name)
                .anyMatch(name -> name.equals(uppercaseValue));
    }
}