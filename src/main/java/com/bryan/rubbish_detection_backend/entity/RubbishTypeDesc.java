package com.bryan.rubbish_detection_backend.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bryan.rubbish_detection_backend.entity.enumeration.WasteTypeEnum;
import com.bryan.rubbish_detection_backend.validator.ValidationGroups;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("rubbish_type_desc")
public class RubbishTypeDesc implements Serializable {
    @NotNull(
            message = "ID不能为空",
            groups = {
                    ValidationGroups.Update.class
            }
    )
    @TableId(type = IdType.AUTO)
    private Integer id;

    @NotNull(message = "垃圾类型不能为空")
    private WasteTypeEnum type;

    @NotNull(message = "垃圾名称不能为空")
    private String name;

    @NotNull(message = "垃圾描述不能为空")
    private String description;

    @JsonIgnore
    private Integer isDeleted;

    // 关联属性
    @TableField(exist = false)
    private List<RubbishTypeDisposalAdvice> disposalAdvice;

    @TableField(exist = false)
    private List<RubbishTypeHandleMethod> handleMethods;

    @TableField(exist = false)
    private List<RubbishTypeCommonThing> commonThings;
}

