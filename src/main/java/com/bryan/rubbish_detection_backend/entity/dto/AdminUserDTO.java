package com.bryan.rubbish_detection_backend.entity.dto;

import com.bryan.rubbish_detection_backend.validator.ValidationGroups;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminUserDTO extends UserDTO {
    @NotBlank(
            groups = ValidationGroups.Create.class,
            message = "创建用户时密码不能为空"
    )
    @Pattern(
            regexp = "^$|^([a-zA-Z0-9]){6,20}$",
            message = "密码只能包含字母和数字，长度为6～20位",
            groups = {Default.class, ValidationGroups.Create.class, ValidationGroups.Update.class}
    )
    private String password;

    @NotNull(message = "参与次数不能为空")
    @Min(value = 0, message = "参与次数不合法")
    private Integer participationCount;

    @NotNull(message = "回收金额不能为空")
    @DecimalMin(value = "0.0", inclusive = true, message = "回收金额不合法")
    @Digits(integer = 10, fraction = 2, message = "回收金额不合法")
    private BigDecimal totalRecycleAmount;
}
