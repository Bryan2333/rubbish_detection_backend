package com.bryan.rubbish_detection_backend.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("rubbish_type_desc")
public class RubbishTypeDesc implements Serializable {
    private Long id;
    private Integer type;
    private String name;
    private String description;

    // 关联属性
    private List<String> disposalAdvice;
    private List<String> handleMethods;
    private List<String> commonThings;
}

