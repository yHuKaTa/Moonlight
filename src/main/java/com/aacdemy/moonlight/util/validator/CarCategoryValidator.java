package com.aacdemy.moonlight.util.validator;

import com.aacdemy.moonlight.entity.car.enums.CarType;
import com.aacdemy.moonlight.util.annotation.ValidCarTypeEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CarCategoryValidator implements ConstraintValidator<ValidCarTypeEnum, String> {
    @Override
    public void initialize(ValidCarTypeEnum constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.equalsIgnoreCase(CarType.SEDAN.getCarType()) ||
                s.equalsIgnoreCase(CarType.SPORT.getCarType()) ||
                s.equalsIgnoreCase(CarType.VAN.getCarType());
    }
}
