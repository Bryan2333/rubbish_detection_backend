package com.bryan.rubbish_detection_backend.entity.dto;

import com.bryan.rubbish_detection_backend.annotation.Base64ImageConstraint;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDTO {
    private Long id;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20之间")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "用户名只能包含字母、数字")
    private String username;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotNull(message = "年龄不能为空")
    @Min(value = 1, message = "年龄不合法")
    @Max(value = 150, message = "年龄不合法")
    private Integer age;

    @NotNull(message = "性别不能为空")
    @Pattern(regexp = "^(男|女|保密)$", message = "性别只能为男、女或保密")
    private String gender;

    @Size(max = 200, message = "个性签名不能超过50个字符")
    private String signature;

    @Base64ImageConstraint
    private String avatar;

    private Integer participationCount;
    private BigDecimal totalRecycleAmount;

    private String role;
}
