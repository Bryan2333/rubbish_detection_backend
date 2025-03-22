package com.bryan.rubbish_detection_backend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bryan.rubbish_detection_backend.annotation.CheckCurrentUser;
import com.bryan.rubbish_detection_backend.entity.RecognitionCollection;
import com.bryan.rubbish_detection_backend.entity.Result;
import com.bryan.rubbish_detection_backend.service.CollectionService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collection")
@SaCheckLogin(type = "user")
public class UserCollectionController {
    @Resource
    private CollectionService collectionService;

    @PostMapping("/findByPage")
    @CheckCurrentUser
    public Result<List<RecognitionCollection>> findByPage(@RequestParam(value = "userId") Long userId,
                                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                          @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        if (userId == null || userId <= 0) {
            return Result.error("-1", "用户ID不合法");
        }

        if (pageNum == null || pageNum <= 0) {
            return Result.error("-1", "页码不能为空");
        }

        if (pageSize == null || pageSize <= 0) {
            return Result.error("-1", "每页数量不能为空");
        }

        List<RecognitionCollection> records = collectionService.findByPage(userId, pageNum, pageSize);

        return Result.success(records);
    }

    @PostMapping("/add")
    @CheckCurrentUser
    public Result<Boolean> addCollection(@Valid @RequestBody RecognitionCollection dto) {
        boolean save = collectionService.saveByUser(dto);
        if (!save) {
            return Result.error("-1", "添加识别收藏识别失败");
        }

        return Result.success();
    }

    @PostMapping("/unCollect")
    @CheckCurrentUser(paramIndex = 1)
    public Result<Boolean> unCollect(@RequestParam("id") Long collectionId,
                                     @RequestParam("userId") Long userId) {
        if (userId == null) {
            return Result.error("-1", "用户ID不能为空");
        }

        if (collectionId == null) {
            return Result.error("-1", "收藏ID不能为空");
        }

        LambdaUpdateWrapper<RecognitionCollection> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(RecognitionCollection::getIsDeleted, 1)
                .eq(RecognitionCollection::getId, collectionId)
                .eq(RecognitionCollection::getUserId, userId);

        boolean removed = collectionService.update(wrapper);
        if (!removed) {
            return Result.error("-1", "取消收藏失败");
        }

        return Result.success();
    }

}

