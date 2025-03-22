package com.bryan.rubbish_detection_backend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bryan.rubbish_detection_backend.entity.PageResult;
import com.bryan.rubbish_detection_backend.entity.Result;
import com.bryan.rubbish_detection_backend.entity.User;
import com.bryan.rubbish_detection_backend.entity.dto.AdminUserDTO;
import com.bryan.rubbish_detection_backend.entity.dto.UserDTO;
import com.bryan.rubbish_detection_backend.service.UserService;
import com.bryan.rubbish_detection_backend.validator.ValidationGroups;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@SaCheckLogin(type = "admin")
public class AdminUserController {
    @Resource
    private UserService userService;

    @PostMapping("/findByPage")
    public Result<PageResult<UserDTO>> findByPage(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                  @RequestParam(value = "username", required = false) String username) {
        if (pageNum < 1 || pageSize < 0) {
            return Result.error("-1", "参数异常");
        }

        PageResult<UserDTO> pageResult = userService.findByPageByAdmin(pageNum, pageSize, username);

        return Result.success(pageResult);
    }


    @PostMapping("/save")
    public Result<UserDTO> saveUser(@Validated({Default.class, ValidationGroups.Create.class}) @RequestBody AdminUserDTO adminUserDTO) {
        boolean saved = userService.createUserByAdmin(adminUserDTO);
        if (!saved) {
            return Result.error("-1", "保存用户失败");
        }

        return Result.success();
    }


    @PostMapping("/update")
    public Result<Boolean> updateUser(@Validated({Default.class, ValidationGroups.Update.class}) @RequestBody AdminUserDTO adminUserDTO) {
        UserDTO updated = userService.updateInfoByAdmin(adminUserDTO);
        if (updated == null) {
            return Result.error("-1", "更新用户失败");
        }

        return Result.success();
    }

    @PostMapping("/delete")
    public Result<Boolean> deleteUser(@RequestParam("userId") Long id) {
        if (id == null) {
            return Result.error("-1", "参数异常");
        }

        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(User::getIsDeleted, 1).eq(User::getId, id);

        boolean deleted = userService.update(wrapper);
        if (!deleted) {
            return Result.error("-1", "删除用户失败");
        }

        return Result.success();
    }

    @Transactional
    @PostMapping("/deleteBatch")
    public Result<Boolean> deleteBatch(@RequestBody List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Result.error("-1", "参数异常");
        }

        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(User::getIsDeleted, 1).in(User::getId, userIds);

        boolean deleted = userService.update(wrapper);
        if (!deleted) {
            return Result.error("-1", "批量删除用户失败");
        }

        return Result.success();
    }
}
