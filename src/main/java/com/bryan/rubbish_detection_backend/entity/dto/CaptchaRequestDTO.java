package com.bryan.rubbish_detection_backend.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.jetbrains.annotations.Contract;

public class CaptchaRequestDTO {
    @Data
    public static class RegisterRequest {
        @NotBlank(message = "邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        private String email;

        public final int serviceType = 0;
    }

    @Data
    public static class ChangeEmailRequest {
        @NotNull(message = "用户名ID不能为空")
        private Long userId;

        @NotBlank(message = "新邮箱为不能空")
        @Email(message = "邮箱格式不正确")
        private String newEmail;

        public final int serviceType = 1;
    }

    @Data
    public static class ChangePasswordRequest {
        @NotNull(message = "用户名ID不能为空")
        private Long userId;

        public final int serviceType = 2;
    }

    @Data
    public static class ForgetPasswordRequest {
        @NotBlank(message = "用户名不能为空")
        @Size(min = 6, max = 20, message = "用户名长度应在6-20之间")
        private String username;

        @NotBlank(message = "邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        private String email;

        public final int serviceType = 3;
    }

    @Contract(pure = true)
    public static @org.jetbrains.annotations.NotNull String getServiceName(int serviceType) {
        return switch (serviceType) {
            case 0 -> "注册新用户";
            case 1 -> "修改邮箱";
            case 2 -> "修改密码";
            case 3 -> "找回密码";
            default -> "未知";
        };
    }
}
