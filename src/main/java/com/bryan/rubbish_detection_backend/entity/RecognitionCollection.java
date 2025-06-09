package com.bryan.rubbish_detection_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bryan.rubbish_detection_backend.annotation.Base64ImageConstraint;
import com.bryan.rubbish_detection_backend.entity.enumeration.WasteTypeEnum;
import com.bryan.rubbish_detection_backend.validator.ValidationGroups;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("recognition_collection")
public class RecognitionCollection implements Serializable {
    @TableId(type = IdType.AUTO)
    @NotNull(
            message = "识别收藏ID不能为空",
            groups = {
                    ValidationGroups.FromUser.class,
                    ValidationGroups.Update.class
            }
    )
    private Long id;


    @NotNull(
            message = "用户ID不能为空",
            groups = {
                    ValidationGroups.FromUser.class,
                    ValidationGroups.Update.class
            }
    )
    private Long userId;

    @NotBlank(
            message = "用户名不能为空",
            groups = {
                    ValidationGroups.FromAdmin.class
            }
    )
    @TableField(exist = false)
    private String username;

    @Base64ImageConstraint
    private String image;

    @NotBlank(message = "垃圾名称不能为空")
    @Size(min = 1, max = 50, message = "垃圾名称长度在1～50之间")
    private String rubbishName;

    @NotNull(message = "垃圾类型不能为空")
    @TableField(value = "rubbish_type")
    private WasteTypeEnum rubbishType;

    @NotNull(message = "收藏时间不能为空")
    private LocalDateTime createdAt;

    private String updatedAt;

    @JsonIgnore
    private Integer isDeleted;
}
