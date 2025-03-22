package com.bryan.rubbish_detection_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bryan.rubbish_detection_backend.annotation.Base64ImageConstraint;
import com.bryan.rubbish_detection_backend.validator.ValidationGroups;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import lombok.Data;

@Data
@TableName("admins")
public class Admin {
    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20之间")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "用户名只能包含字母、数字")
    private String username;

    @NotBlank(
            groups = ValidationGroups.Create.class,
            message = "创建用户时密码不能为空"
    )
    @Pattern(
            regexp = "^$|^([a-zA-Z0-9]){6,20}$",
            message = "密码只能包含字母和数字，长度为6～20位",
            groups = {
                    Default.class,
                    ValidationGroups.Create.class,
                    ValidationGroups.Update.class
            }
    )
    @JsonIgnore
    private String password;

    @NotNull(message = "年龄不能为空")
    @Min(value = 1, message = "年龄不合法")
    @Max(value = 150, message = "年龄不合法")
    private Integer age;

    @NotBlank(message = "性别不能为空")
    @Pattern(regexp = "^(男|女|保密)$", message = "性别只能为男、女或保密")
    private String gender;

    @NotBlank(message = "手机号不能为空")
    @Pattern(
            regexp = "^(?:\\+?86)?1(?:3\\d{3}|5[^4\\D]\\d{2}|8\\d{3}|7(?:[235-8]\\d{2}|4(?:0\\d|1[0-2]|9\\d))|9[0-35-9]\\d{2}|66\\d{2})\\d{6}$",
            message = "手机号码不合法"
    )
    private String phone;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Base64ImageConstraint
    private String avatar;

    @JsonIgnore
    private Integer isDeleted;
}
