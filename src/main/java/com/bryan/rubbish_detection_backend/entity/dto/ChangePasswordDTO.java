package com.bryan.rubbish_detection_backend.entity.dto;

import com.bryan.rubbish_detection_backend.entity.enumeration.CaptchaServiceTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Objects;

@Data
public class ChangePasswordDTO {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
    private String newPassword;

    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    @NotBlank(message = "验证码不能为空")
    @Size(min = 6, max = 6, message = "验证码长度必须为6位")
    private String verifyCode;

    public final CaptchaServiceTypeEnum serviceType = CaptchaServiceTypeEnum.CHANGE_PASSWORD;

    public boolean isPasswordConfirmed() {
        return Objects.equals(newPassword, confirmPassword);
    }
}
