package com.bryan.rubbish_detection_backend.entity.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {
    /**
     * 待处理
     */
    PENDING(0, "待处理"),
    /**
     * 处理中
     */
    IN_PROGRESS(1, "处理中"),
    /**
     * 已完成
     */
    COMPLETED(2, "已完成"),
    /**
     * 已取消
     */
    CANCELED(3, "已取消");

    @JsonValue
    @EnumValue
    private final int statusCode;

    private final String statusName;
}
