package com.aacdemy.moonlight.util.image;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class ImageValidator implements ConstraintValidator<ValidImage, MultipartFile[]> {
    @Override
    public void initialize(ValidImage constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile[] multipartFiles, ConstraintValidatorContext context) {
        boolean result = true;
        if (Objects.isNull(multipartFiles)) {
            context.buildConstraintViolationWithTemplate("Select at least 1 image.").addConstraintViolation();
            result = false;
        }
        if (multipartFiles.length > 3) {
            context.buildConstraintViolationWithTemplate("Max 3 JPG images are allowed.").addConstraintViolation();
            result = false;
        }
        for (MultipartFile file : multipartFiles) {
            if (Objects.isNull(file) || file.isEmpty() || file.getSize() == 0) {
                context.buildConstraintViolationWithTemplate("Select at least 1 image.").addConstraintViolation();
                result = false;
            }
            if (!Objects.requireNonNull(file.getContentType()).equalsIgnoreCase("image/jpeg")) {
                context.buildConstraintViolationWithTemplate("Only JPG images are allowed.").addConstraintViolation();
                result = false;
            }
        }
        return result;
    }
}
