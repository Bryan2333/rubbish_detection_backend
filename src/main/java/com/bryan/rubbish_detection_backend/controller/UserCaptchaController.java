package com.bryan.rubbish_detection_backend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.bryan.rubbish_detection_backend.annotation.CheckCurrentUser;
import com.bryan.rubbish_detection_backend.entity.Result;
import com.bryan.rubbish_detection_backend.entity.dto.CaptchaRequestDTO;
import com.bryan.rubbish_detection_backend.service.CaptchaService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/captcha")
public class UserCaptchaController {
    @Resource
    private CaptchaService captchaService;

    @PostMapping("/register")
    public Result<Boolean> sendRegisterVerifyCode(@Valid @RequestBody CaptchaRequestDTO.RegisterRequest request) {
        captchaService.sendRegisterVerifyCode(request);

        return Result.success();
    }

    @SaCheckLogin(type = "user")
    @PostMapping("/changePassword")
    public Result<Boolean> sendChangePasswordVerifyCode(@Valid @RequestBody @CheckCurrentUser CaptchaRequestDTO.ChangePasswordRequest request) {
        captchaService.sendChangePasswordVerifyCode(request);

        return Result.success();
    }

    @SaCheckLogin(type = "user")
    @PostMapping("/changeEmail")
    public Result<Boolean> sendChangeEmailVerifyCode(@Valid @RequestBody @CheckCurrentUser CaptchaRequestDTO.ChangeEmailRequest request) {
        captchaService.sendChangeEmailVerifyCode(request);

        return Result.success();
    }

    @PostMapping("/resetPassword")
    public Result<Boolean> sendResetPasswordVerifyCode(@Valid @RequestBody CaptchaRequestDTO.ForgetPasswordRequest request) {
        captchaService.sendForgetPasswordVerifyCode(request);

        return Result.success();
    }
}
