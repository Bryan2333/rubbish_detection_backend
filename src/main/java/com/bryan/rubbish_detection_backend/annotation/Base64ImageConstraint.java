package com.bryan.rubbish_detection_backend.annotation;

import com.bryan.rubbish_detection_backend.validator.Base64ImageValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = Base64ImageValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Base64ImageConstraint {
    String message() default "无效的Base64图片格式";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
