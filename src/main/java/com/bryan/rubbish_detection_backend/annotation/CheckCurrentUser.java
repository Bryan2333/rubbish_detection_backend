package com.bryan.rubbish_detection_backend.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface CheckCurrentUser {
    /**
     * 需要校验的字段，默认值是 userId
     */
    String value() default "userId";


    /**
     * 当注解用在方法上时，指定需要校验的参数在方法参数列表中的索引
     * 如果设置为 -1，则表示未指定（推荐在方法级别使用时明确指定 paramIndex）
     */
    int paramIndex() default 0;

    /**
     * 指定校验的用户角色，默认为普通用户
     */
    Role role() default Role.USER;

    enum Role {
        USER,
        ADMIN
    }
}
