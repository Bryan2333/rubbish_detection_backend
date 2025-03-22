package com.bryan.rubbish_detection_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bryan.rubbish_detection_backend.validator.ValidationGroups;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("addresses")
public class Address implements Serializable {
    @TableId(type = IdType.AUTO)
    @NotNull(message = "地址ID不能为空", groups = {ValidationGroups.Update.class})
    private Long id;

    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotBlank(message = "手机号不能为空")
    @Pattern(
            regexp = "^(?:\\+?86)?1(?:3\\d{3}|5[^4\\D]\\d{2}|8\\d{3}|7(?:[0-35-9]\\d{2}|4(?:0\\d|1[0-2]|9\\d))|9[0-35-9]\\d{2}|6[2567]\\d{2}|4(?:(?:10|4[01])\\d{3}|[68]\\d{4}|[579]\\d{2}))\\d{6}$",
            message = "手机号格式不正确"
    )
    private String phoneNum;

    @NotBlank(message = "省份不能为空")
    private String province;

    @NotBlank(message = "城市不能为空")
    private String city;

    @NotBlank(message = "区域不能为空")
    private String area;

    @NotBlank(message = "详细地址不能为空")
    private String detail;

    @NotNull(message = "上门时间不能为空")
    private String pickupTime;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
