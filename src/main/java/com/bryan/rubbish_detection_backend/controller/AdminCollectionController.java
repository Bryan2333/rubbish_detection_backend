package com.bryan.rubbish_detection_backend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bryan.rubbish_detection_backend.entity.PageResult;
import com.bryan.rubbish_detection_backend.entity.RecognitionCollection;
import com.bryan.rubbish_detection_backend.entity.Result;
import com.bryan.rubbish_detection_backend.exception.CustomException;
import com.bryan.rubbish_detection_backend.service.CollectionService;
import com.bryan.rubbish_detection_backend.validator.ValidationGroups;
import jakarta.annotation.Resource;
import jakarta.validation.groups.Default;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/collection")
@SaCheckLogin(type = "admin")
public class AdminCollectionController {
    @Resource
    private CollectionService collectionService;

    @PostMapping("/findByPage")
    public Result<PageResult<RecognitionCollection>> findByPage(@RequestParam(value = "username", required = false) String username,
                                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        if (pageNum < 1 || pageSize < 0) {
            return Result.error("-1", "参数异常");
        }

        PageResult<RecognitionCollection> pageResult = collectionService.findByPageByAdmin(username, pageNum, pageSize);

        return Result.success(pageResult);
    }

    @PostMapping("/save")
    public Result<Object> save(@Validated({ValidationGroups.FromAdmin.class}) @RequestBody RecognitionCollection dto) {
        if (dto == null) throw new CustomException("参数异常");

        Boolean saved = collectionService.saveByAdmin(dto);
        if (!saved) {
            return Result.error("-1", "保存识别收藏失败");
        }

        return Result.success();
    }

    @PostMapping("/update")
    public Result<Object> update(@Validated({Default.class, ValidationGroups.FromAdmin.class}) @RequestBody RecognitionCollection dto) {
        if (dto == null) throw new CustomException("参数异常");

        Boolean updated = collectionService.updateByAdmin(dto);
        if (!updated) {
            return Result.error("-1", "更新识别收藏失败");
        }

        return Result.success();
    }

    @PostMapping("/delete")
    public Result<Object> delete(@RequestParam("collectionId") Long id) {
        if (id == null) throw new CustomException("参数异常");

        LambdaUpdateWrapper<RecognitionCollection> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(RecognitionCollection::getIsDeleted, 1).eq(RecognitionCollection::getId, id);

        boolean deleted = collectionService.update(wrapper);
        if (!deleted) {
            return Result.error("-1", "删除识别收藏失败");
        }

        return Result.success();
    }

    @Transactional
    @PostMapping("/deleteBatch")
    public Result<Object> deleteBatch(@RequestBody List<Long> collectionIds) {
        if (collectionIds == null || collectionIds.isEmpty()) throw new CustomException("参数异常");

        LambdaUpdateWrapper<RecognitionCollection> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(RecognitionCollection::getIsDeleted, 1).in(RecognitionCollection::getId, collectionIds);

        boolean deleted = collectionService.update(wrapper);
        if (!deleted) {
            return Result.error("-1", "批量删除识别收藏失败");
        }

        return Result.success();
    }
}
