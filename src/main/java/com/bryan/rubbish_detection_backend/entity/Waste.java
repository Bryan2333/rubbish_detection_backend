package com.bryan.rubbish_detection_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bryan.rubbish_detection_backend.validator.ValidationGroups;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("wastes")
public class Waste implements Serializable {
    @TableId(type = IdType.AUTO)
    @NotNull(message = "垃圾ID不能为空", groups = {ValidationGroups.Update.class})
    private Long id;

    @NotNull(message = "废品类型不能为空")
    private Integer type;

    @NotBlank(message = "废品名称不能为空")
    @Size(max = 20, message = "废品名称不能超过20个字符")
    private String name;

    @NotNull(message = "废品重量不能为空")
    @Min(value = 0, message = "废品重量不能小于0")
    private Double weight;

    @NotNull(message = "废品单位不能为空")
    private Integer unit;

    @Size(max = 255, message = "废品描述不能超过255个字符")
    private String description;

    @NotNull(message = "废品图片不能为空")
    @Valid
    @TableField(exist = false)
    private List<WastePhoto> photos;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
