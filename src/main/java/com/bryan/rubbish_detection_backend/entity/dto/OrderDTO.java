package com.bryan.rubbish_detection_backend.entity.dto;

import com.bryan.rubbish_detection_backend.entity.Address;
import com.bryan.rubbish_detection_backend.entity.Waste;
import com.bryan.rubbish_detection_backend.entity.enumeration.OrderStatusEnum;
import com.bryan.rubbish_detection_backend.validator.ValidationGroups;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDTO implements Serializable {
    @NotNull(message = "订单ID不能为空", groups = {ValidationGroups.Update.class})
    private Long id;

    @NotNull(
            message = "用户ID不能为空",
            groups = {
                    ValidationGroups.FromUser.class,
                    ValidationGroups.Update.class
            }
    )
    private Long userId;

    @NotNull(
            message = "用户ID不能为空",
            groups = {
                    ValidationGroups.FromAdmin.class,
                    ValidationGroups.Update.class
            }
    )
    private String username;

    @NotNull(message = "废品信息不能为空", groups = {ValidationGroups.FromUser.class})
    @Valid
    private Waste waste;

    @NotNull(message = "地址不能为空")
    @Valid
    private Address address;

    @NotNull(message = "订单提交时间不能为空")
    private LocalDateTime orderDate;

    @NotNull(message = "订单状态不能为空")
    private OrderStatusEnum orderStatus;

    @NotNull(message = "预估价格不能为空")
    private BigDecimal estimatedPrice;

    @Min(value = 0, message = "实际价格不能小于0")
    private BigDecimal actualPrice;

    @Min(value = 0, message = "评价等级不能小于0")
    @Max(value = 5, message = "评价等级不能大于5")
    private Integer reviewRate;

    @Size(max = 50, message = "评价信息不能超过50个字符")
    private String reviewMessage;
}
