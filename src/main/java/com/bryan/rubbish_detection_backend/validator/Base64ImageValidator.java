package com.bryan.rubbish_detection_backend.validator;

import com.bryan.rubbish_detection_backend.annotation.Base64ImageConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.util.Objects;

public class Base64ImageValidator implements ConstraintValidator<Base64ImageConstraint, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!StringUtils.hasText(value)) {
            return true;
        }

        // 如果是前端传过来的删除标记，直接返回true
        if (Objects.equals("needDelete", value)) {
            return true;
        }

        return value.startsWith("data:image") && value.contains("base64,");
    }
}
