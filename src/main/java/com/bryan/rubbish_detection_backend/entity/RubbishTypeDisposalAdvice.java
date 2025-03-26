package com.bryan.rubbish_detection_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@TableName("rubbish_type_disposal_advice")
public class RubbishTypeDisposalAdvice implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer rubbishId;

    private String advice;

    @JsonIgnore
    private Integer isDeleted;
}
