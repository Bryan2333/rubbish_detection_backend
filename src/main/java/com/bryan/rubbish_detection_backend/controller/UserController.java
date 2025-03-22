package com.bryan.rubbish_detection_backend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.bryan.rubbish_detection_backend.annotation.CheckCurrentUser;
import com.bryan.rubbish_detection_backend.entity.Result;
import com.bryan.rubbish_detection_backend.entity.dto.*;
import com.bryan.rubbish_detection_backend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@SaCheckLogin(type = "user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/updateInfo")
    @CheckCurrentUser(value = "id")
    public Result<UserDTO> updateInfo(@RequestBody UserDTO userDTO) {
        UserDTO updated = userService.updateInfo(userDTO);
        if (updated == null) {
            return Result.error("-1", "更新用户信息失败");
        }

        return Result.success(updated);
    }

    @PostMapping("/changePassword")
    @CheckCurrentUser
    public Result<Boolean> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        Boolean changed = userService.changePassword(changePasswordDTO);
        if (!changed) {
            return Result.error("-1", "修改密码失败");
        }

        return Result.success(true);
    }

    @PostMapping("/changeEmail")
    @CheckCurrentUser
    public Result<Boolean> changeEmail(@Valid @RequestBody ChangeEmailDTO changeEmailDTO) {
        Boolean changed = userService.changeEmail(changeEmailDTO);
        if (!changed) {
            return Result.error("-1", "修改邮箱失败");
        }

        return Result.success(true);
    }

    @SaIgnore
    @PostMapping("/resetPassword")
    public Result<Boolean> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        Boolean reset = userService.resetPassword(resetPasswordDTO);

        if (!reset) {
            return Result.error("-1", "重置密码失败");
        }
        return Result.success(true);
    }
}
