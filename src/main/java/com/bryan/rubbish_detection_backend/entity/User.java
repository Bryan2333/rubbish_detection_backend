package com.bryan.rubbish_detection_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("users")
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    // 用户名，登录时使用
    private String username;

    // 邮箱地址
    private String email;

    // 密码
    @JsonIgnore
    private String password;

    // 年龄
    private Integer age;

    // 性别，例如 "男", "女", "保密"
    private String gender;

    // 个性签名
    private String signature;

    // 存放头像图片路径
    private String avatar;

    // 参与回收次数
    private Integer participationCount;

    // 累计回收金额
    private BigDecimal totalRecycleAmount;

    // 用户角色
    private String role;

    // 记录创建时间
    private LocalDateTime createdAt;

    // 记录更新时间
    private LocalDateTime updatedAt;

    // 逻辑删除标志，0表示未删除，1表示已删除
    private Integer isDeleted;
}
