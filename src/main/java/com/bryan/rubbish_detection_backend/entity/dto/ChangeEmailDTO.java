package com.bryan.rubbish_detection_backend.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangeEmailDTO {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotBlank(message = "新邮箱不能为空")
    @Email
    private String newEmail;

    @NotBlank(message = "验证码不能为空")
    @Size(min = 6, max = 6, message = "验证码长度必须为6位")
    private String verifyCode;

    public final int serviceType = 1;
}
