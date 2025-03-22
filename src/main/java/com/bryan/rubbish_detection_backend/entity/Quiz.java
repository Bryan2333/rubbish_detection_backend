package com.bryan.rubbish_detection_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bryan.rubbish_detection_backend.validator.ValidationGroups;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("quiz")
public class Quiz {
    @NotNull(
            message = "题目ID不能为空",
            groups = {
                    ValidationGroups.Update.class
            }
    )
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "题目内容不能为空")
    @Size(max = 50, message = "题目内容长度不能超过50")
    private String question;        // 题目内容

    @NotBlank(message = "选项A不能为空")
    @Size(max = 50, message = "选项A长度不能超过50")
    private String optionA;        // 选项A

    @NotBlank(message = "选项B不能为空")
    @Size(max = 50, message = "选项B长度不能超过50")
    private String optionB;        // 选项B

    @NotBlank(message = "选项C不能为空")
    @Size(max = 50, message = "选项C长度不能超过50")
    private String optionC;        // 选项C

    @NotBlank(message = "选项D不能为空")
    @Size(max = 50, message = "选项D长度不能超过50")
    private String optionD;        // 选项D

    @NotNull(message = "正确答案不能为空")
    @Min(value = 0, message = "正确答案索引不能小于0")
    @Max(value = 3, message = "正确答案索引不能大于3")
    private Integer correctAnswerIndex;  // 正确答案索引

    @JsonIgnore
    private Integer status;        // 状态

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @JsonIgnore
    private Integer isDeleted;
}
