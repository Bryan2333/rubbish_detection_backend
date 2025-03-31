package com.bryan.rubbish_detection_backend.entity.dto;

import com.bryan.rubbish_detection_backend.entity.enumeration.CaptchaServiceTypeEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class CaptchaRequestDTO {
    @Data
    public static class RegisterRequest {
        @NotBlank(message = "邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        private String email;

        public final CaptchaServiceTypeEnum serviceType = CaptchaServiceTypeEnum.REGISTER;
    }

    @Data
    public static class ChangeEmailRequest {
        @NotNull(message = "用户名ID不能为空")
        private Long userId;

        @NotBlank(message = "新邮箱为不能空")
        @Email(message = "邮箱格式不正确")
        private String newEmail;

        public final CaptchaServiceTypeEnum serviceType = CaptchaServiceTypeEnum.CHANGE_EMAIL;
    }

    @Data
    public static class ChangePasswordRequest {
        @NotNull(message = "用户名ID不能为空")
        private Long userId;

        public final CaptchaServiceTypeEnum serviceType = CaptchaServiceTypeEnum.CHANGE_PASSWORD;
    }

    @Data
    public static class ForgetPasswordRequest {
        @NotBlank(message = "用户名不能为空")
        @Size(min = 6, max = 20, message = "用户名长度应在6-20之间")
        private String username;

        @NotBlank(message = "邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        private String email;

        public final CaptchaServiceTypeEnum serviceType = CaptchaServiceTypeEnum.FORGOT_PASSWORD;
    }
}
