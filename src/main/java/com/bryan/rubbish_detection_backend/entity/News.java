package com.bryan.rubbish_detection_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bryan.rubbish_detection_backend.validator.ValidationGroups;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("news")
public class News implements Serializable {
    @NotNull(
            message = "新闻ID不能为空",
            groups = {
                    ValidationGroups.Update.class
            }
    )
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;          // 主键ID

    @NotBlank(message = "文章链接不能为空")
    private String url;       // 文章链接

    @NotBlank(message = "新闻图片链接不能为空")
    private String imageUrl;  // 图片链接

    @NotBlank(message = "文章标题不能为空")
    private String title;     // 文章标题

    @NotBlank(message = "文章内容不能为空")
    private String author;    // 作者

    @NotNull(message = "发布时间不能为空")
    private LocalDateTime publishTime; // 创建时间

    @JsonIgnore
    private Integer isDeleted;
}
