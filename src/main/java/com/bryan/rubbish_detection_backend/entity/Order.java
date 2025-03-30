package com.bryan.rubbish_detection_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    @TableField(exist = false)
    private Waste waste;
    @TableField(exist = false)
    private Address address;
    private LocalDateTime orderDate;
    private Integer orderStatus;
    private BigDecimal estimatedPrice;
    private BigDecimal actualPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer reviewRate;
    private String reviewMessage;

    private Integer isDeleted;

    private Long addressId;
    private Long wasteId;

    @TableField(exist = false)
    private String username;
}
