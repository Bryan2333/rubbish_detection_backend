package com.bryan.rubbish_detection_backend.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bryan.rubbish_detection_backend.entity.Feedback;
import com.bryan.rubbish_detection_backend.entity.PageResult;
import com.bryan.rubbish_detection_backend.entity.Result;
import com.bryan.rubbish_detection_backend.exception.CustomException;
import com.bryan.rubbish_detection_backend.service.FeedbackService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/feedback")
public class AdminFeedbackController {
    @Resource
    private FeedbackService feedbackService;

    @PostMapping("/findByPage")
    public Result<PageResult<Feedback>> findByPage(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                   @RequestParam(value = "name", required = false) String name) {
        if (pageNum < 1 || pageSize < 0) {
            return Result.error("-1", "参数异常");
        }

        PageResult<Feedback> pageResult = feedbackService.findByPageByAdmin(name, pageNum, pageSize);

        return Result.success(pageResult);
    }

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody Feedback dto) {
        if (dto == null) throw new CustomException("参数异常");

        boolean saved = feedbackService.saveByAdmin(dto);
        if (!saved) {
            return Result.error("-1", "反馈信息保存失败");
        }

        return Result.success();
    }

    @PostMapping("/update")
    public Result<Object> update(@Valid @RequestBody Feedback dto) {
        if (dto == null) throw new CustomException("参数异常");

        boolean updated = feedbackService.updateByAdmin(dto);
        if (!updated) {
            return Result.error("-1", "反馈信息更新失败");
        }

        return Result.success();
    }

    @PostMapping("/delete")
    public Result<Object> delete(@RequestParam("feedbackId") Long id) {
        if (id == null) throw new CustomException("参数异常");

        LambdaUpdateWrapper<Feedback> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Feedback::getIsDeleted, 1).eq(Feedback::getId, id);

        boolean deleted = feedbackService.update(wrapper);
        if (!deleted) {
            return Result.error("-1", "反馈信息删除失败");
        }

        return Result.success();
    }

    @Transactional
    @PostMapping("/deleteBatch")
    public Result<Object> deleteBatch(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) throw new CustomException("参数异常");

        LambdaUpdateWrapper<Feedback> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Feedback::getIsDeleted, 1).in(Feedback::getId, ids);

        boolean deleted = feedbackService.update(wrapper);
        if (!deleted) {
            return Result.error("-1", "反馈信息批量删除失败");
        }

        return Result.success();
    }
}
