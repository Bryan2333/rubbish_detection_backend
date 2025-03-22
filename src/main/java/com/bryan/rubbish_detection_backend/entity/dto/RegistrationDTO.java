package com.bryan.rubbish_detection_backend.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RegistrationDTO extends UserDTO {
    @NotBlank(message = "密码不能为空")
    @Pattern(
            regexp = "^[a-zA-Z0-9]{6,20}$",
            message = "密码只能包含数字、字母，长度为6～20位"
    )
    private String password;

    @NotBlank(message = "验证码不能为空")
    @Size(min = 6, max = 6, message = "验证码长度必须为6")
    private String verifyCode;

    public final int serviceType = 0;
}
