package com.bryan.rubbish_detection_backend.entity.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
@AllArgsConstructor
public enum CaptchaServiceTypeEnum {
    /**
     * 用户注册
     */
    REGISTER(0,"注册新用户"),

    /**
     * 修改邮箱
     */
    CHANGE_EMAIL(1,"修改邮箱"),

    /**
     * 修改密码
     */
    CHANGE_PASSWORD(2,"修改密码"),

    /**
     * 忘记密码
     */
    FORGOT_PASSWORD(3,"忘记密码");

    @JsonValue
    @EnumValue
    private final int type;

    private final String name;

    public static @Nullable String getDescription(int type) {
        for (CaptchaServiceTypeEnum value : CaptchaServiceTypeEnum.values()) {
            if (value.getType() == type) {
                return value.getName();
            }
        }
        return null;
    }
}
