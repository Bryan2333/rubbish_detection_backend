package com.bryan.rubbish_detection_backend.controller;

import com.bryan.rubbish_detection_backend.entity.Admin;
import com.bryan.rubbish_detection_backend.entity.Result;
import com.bryan.rubbish_detection_backend.entity.User;
import com.bryan.rubbish_detection_backend.entity.dto.LoginDTO;
import com.bryan.rubbish_detection_backend.entity.dto.RegistrationDTO;
import com.bryan.rubbish_detection_backend.entity.dto.UserDTO;
import com.bryan.rubbish_detection_backend.exception.CustomException;
import com.bryan.rubbish_detection_backend.service.AdminService;
import com.bryan.rubbish_detection_backend.service.UserService;
import com.bryan.rubbish_detection_backend.utils.StpKit;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api")
public class WebController {
    @Resource
    private UserService userService;

    @Resource
    private AdminService adminService;

    @PostMapping("/register")
    public Result<Boolean> registerUser(@RequestBody RegistrationDTO dto) {
        if (dto == null) throw new CustomException("参数异常");

        boolean registered = userService.createUser(dto);
        if (!registered) {
            return Result.error("-1", "注册失败");
        }

        return Result.success();
    }

    @PostMapping("/login")
    public Result<Object> loginUser(@Valid @RequestBody LoginDTO dto) {
        if (dto == null) throw new CustomException("参数异常");

        if (Objects.equals("0", dto.getRole())) {
            User user = userService.doLogin(dto);
            if (user == null) {
                return Result.error("-1", "用户名或密码错误");
            }

            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);

            StpKit.USER.login(user.getId());

            return Result.success(userDTO);
        }

        if (Objects.equals("2", dto.getRole())) {
            Admin admin = adminService.doLogin(dto);
            if (admin == null) {
                return Result.error("-1", "用户名或密码错误");
            }

            StpKit.ADMIN.login(admin.getId());

            return Result.success(admin);
        }

        return Result.error("-1", "系统异常");
    }

    @PostMapping("/logout")
    public Result<Boolean> logoutUser(@RequestParam("role") String role) {
        if (!StringUtils.hasText(role)) {
            throw new CustomException("参数异常");
        }

        if (Objects.equals("0", role)) {
            StpKit.USER.checkLogin();
            StpKit.USER.logout();
            return Result.success();
        }

        if (Objects.equals("2", role)) {
            StpKit.ADMIN.checkLogin();
            StpKit.ADMIN.logout();
            return Result.success();
        }

        return Result.error("-1", "系统异常");
    }
}
