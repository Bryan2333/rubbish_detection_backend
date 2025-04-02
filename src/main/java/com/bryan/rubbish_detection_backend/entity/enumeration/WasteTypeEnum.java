package com.bryan.rubbish_detection_backend.entity.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum WasteTypeEnum {
    /**
     * 干垃圾
     */
    DRY(0, "干垃圾"),
    /**
     * 湿垃圾
     */
    WET(1, "湿垃圾"),
    /**
     * 可回收物
     */
    RECYCLE(2, "可回收物"),
    /**
     * 有害垃圾
     */
    HARMFUL(3, "有害垃圾");

    @EnumValue
    @JsonValue
    private final int type;

    private final String name;

    public static @Nullable String getNameByType(int type) {
        return Arrays
                .stream(WasteTypeEnum.values())
                .filter((wasteType) -> Objects.equals(wasteType.getType(), type))
                .map(WasteTypeEnum::getName)
                .findFirst()
                .orElse(null);
    }
}
