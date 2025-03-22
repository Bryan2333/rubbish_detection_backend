package com.bryan.rubbish_detection_backend.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "身份不能为空")
    private String role; // 0：用户 1：回收员 2：管理员
}
