package com.bryan.rubbish_detection_backend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bryan.rubbish_detection_backend.entity.Result;
import com.bryan.rubbish_detection_backend.entity.RubbishTypeDesc;
import com.bryan.rubbish_detection_backend.service.RubbishTypeDescService;
import com.bryan.rubbish_detection_backend.validator.ValidationGroups;
import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SaCheckLogin(type = "admin")
public class RubbishTypeDescController {
    @Resource
    private RubbishTypeDescService rubbishTypeDescService;

    @SaIgnore
    @GetMapping("/api/rubbish-type/getDesc")
    public Result<RubbishTypeDesc> getCompleteInfo(@RequestParam("type") Integer type) {
        RubbishTypeDesc desc = rubbishTypeDescService.getCompleteInfoByType(type);
        return Result.success(desc);
    }

    @Transactional
    @PostMapping("/api/admin/rubbish-type/findByList")
    public Result<List<RubbishTypeDesc>> findByList(@RequestParam(value = "name", required = false) String name) {
        List<RubbishTypeDesc> byList = rubbishTypeDescService.findByList(name);

        if (byList == null || byList.isEmpty()) {
            return Result.error("-1", "未找到相关信息");
        }

        return Result.success(byList);
    }

    @Transactional
    @PostMapping("/api/admin/rubbish-type/save")
    public Result<String> save(@Validated({ValidationGroups.Create.class}) @RequestBody RubbishTypeDesc rubbishTypeDesc) {
        boolean save = rubbishTypeDescService.saveByAdmin(rubbishTypeDesc);

        if (!save) {
            return Result.error("-1", "保存垃圾分类指南信息失败");
        }
        return Result.success();
    }

    @Transactional
    @PostMapping("/api/admin/rubbish-type/update")
    public Result<String> update(@Validated({ValidationGroups.Update.class}) @RequestBody RubbishTypeDesc rubbishTypeDesc) {
        boolean update = rubbishTypeDescService.updateByAdmin(rubbishTypeDesc);

        if (!update) {
            return Result.error("-1", "更新垃圾分类指南信息失败");
        }
        return Result.success();
    }

    @Transactional
    @PostMapping("/api/admin/rubbish-type/delete")
    public Result<String> delete(@RequestParam("rubbishDescId") Integer id) {
        if (id == null || id < 0) {
            return Result.error("-1", "参数异常");
        }

        LambdaUpdateWrapper<RubbishTypeDesc> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(RubbishTypeDesc::getIsDeleted, 1).eq(RubbishTypeDesc::getId, id).eq(RubbishTypeDesc::getIsDeleted, 0);

        boolean deleted = rubbishTypeDescService.update(wrapper);
        if (!deleted) {
            return Result.error("-1", "删除垃圾分类指南信息失败");
        }
        return Result.success();
    }

    @Transactional
    @PostMapping("/api/admin/rubbish-type/deleteBatch")
    public Result<String> deleteBatch(@RequestBody List<Integer> rubbishDescIds) {
        if (rubbishDescIds == null || rubbishDescIds.isEmpty()) {
            return Result.error("-1", "参数异常");
        }

        LambdaUpdateWrapper<RubbishTypeDesc> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(RubbishTypeDesc::getIsDeleted, 1).in(RubbishTypeDesc::getId, rubbishDescIds).eq(RubbishTypeDesc::getIsDeleted, 0);

        boolean deleted = rubbishTypeDescService.update(wrapper);
        if (!deleted) {
            return Result.error("-1", "批量删除垃圾分类指南信息失败");
        }
        return Result.success();
    }
}
