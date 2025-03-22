package com.bryan.rubbish_detection_backend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bryan.rubbish_detection_backend.annotation.CheckCurrentUser;
import com.bryan.rubbish_detection_backend.entity.Admin;
import com.bryan.rubbish_detection_backend.entity.PageResult;
import com.bryan.rubbish_detection_backend.entity.Result;
import com.bryan.rubbish_detection_backend.exception.CustomException;
import com.bryan.rubbish_detection_backend.service.AdminService;
import com.bryan.rubbish_detection_backend.utils.StpKit;
import com.bryan.rubbish_detection_backend.validator.ValidationGroups;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@SaCheckLogin(type = "admin")
public class AdminController {
    @Resource
    private AdminService adminService;

    @PostMapping("/findByPage")
    public Result<PageResult<Admin>> getUserByPage(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                      @RequestParam(value = "username", required = false) String username) {
        if (pageNum < 1 || pageSize < 0) {
            return Result.error("-1", "参数异常");
        }

        PageResult<Admin> pageResult = adminService.findByPageByAdmin(pageNum, pageSize, username);

        return Result.success(pageResult);
    }

    @PostMapping("/save")
    public Result<String> save(@Validated({Default.class, ValidationGroups.Create.class}) @RequestBody Admin admin) {
        if (admin == null) throw new CustomException("参数异常");

        Boolean saved = adminService.createAdmin(admin);
        if (!saved) {
            return Result.error("-1", "创建管理员失败");
        }

        return Result.success();
    }

    @PostMapping("/update")
    public Result<Admin> update(@Validated({Default.class, ValidationGroups.Update.class}) @RequestBody Admin admin) {
        if (admin == null) throw new CustomException("参数异常");

        Admin updatedAdmin = adminService.updateAdmin(admin);
        if (updatedAdmin == null) {
            return Result.error("-1", "更新管理员信息失败");
        }

        return Result.success(admin);
    }

    @PostMapping("/delete")
    public Result<String> delete(@RequestParam("adminId") Long id) {
        if (id == null) throw new CustomException("参数异常");

        LambdaUpdateWrapper<Admin> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Admin::getIsDeleted, 1).eq(Admin::getId, id);

        boolean deleted = adminService.update(wrapper);
        if (!deleted) {
            return Result.error("-1", "删除管理员失败");
        }

        return Result.success();
    }

    @Transactional
    @PostMapping("/deleteBatch")
    public Result<String> deleteBatch(@RequestBody List<Long> adminIds) {
        if (adminIds == null || adminIds.isEmpty()) throw new CustomException("参数异常");

        LambdaUpdateWrapper<Admin> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Admin::getIsDeleted, 1).in(Admin::getId, adminIds);

        boolean deleted = adminService.update(wrapper);
        if (!deleted) {
            return Result.error("-1", "批量删除管理员失败");
        }

        return Result.success();
    }


    @PostMapping("/updatePassword")
    @CheckCurrentUser(role = CheckCurrentUser.Role.ADMIN)
    public Result<String> updatePassword(@RequestParam("adminId") Long adminId,
                                         @RequestParam("oldPassword") String oldPassword,
                                         @RequestParam("newPassword")
                                         @Pattern(regexp = "^([a-zA-Z0-9]){6,20}$", message = "密码只能包含字母和数字，长度为6～20位") String newPassword,
                                         @RequestParam("confirmPassword") String confirmPassword) {

        boolean updated = adminService.updatePassword(adminId, oldPassword, newPassword, confirmPassword);
        if (!updated) {
            return Result.error("-1", "修改密码失败");
        }

        StpKit.ADMIN.logout();

        return Result.success();
    }
}
