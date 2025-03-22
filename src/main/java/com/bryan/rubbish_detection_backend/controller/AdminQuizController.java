package com.bryan.rubbish_detection_backend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bryan.rubbish_detection_backend.entity.PageResult;
import com.bryan.rubbish_detection_backend.entity.Quiz;
import com.bryan.rubbish_detection_backend.entity.Result;
import com.bryan.rubbish_detection_backend.exception.CustomException;
import com.bryan.rubbish_detection_backend.service.QuizService;
import com.bryan.rubbish_detection_backend.validator.ValidationGroups;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/quiz")
@SaCheckLogin(type = "admin")
public class AdminQuizController {
    @Resource
    private QuizService quizService;

    @PostMapping("/findByPage")
    public Result<PageResult<Quiz>> findByPage(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                  @RequestParam(value = "question", required = false) String question) {
        if (pageNum < 1 || pageSize < 0) {
            return Result.error("-1", "参数异常");
        }

        PageResult<Quiz> page = quizService.findByPageByAdmin(question, pageNum, pageSize);

        return Result.success(page);
    }

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody Quiz dto) {
        if (dto == null) throw new CustomException("参数异常");

        boolean saved = quizService.saveByAdmin(dto);
        if (!saved) {
            return Result.error("-1", "题目信息保存失败");
        }

        return Result.success();
    }

    @PostMapping("/update")
    public Result<Object> update(@Validated({Default.class, ValidationGroups.Update.class}) @RequestBody Quiz dto) {
        if (dto == null) throw new CustomException("参数异常");

        boolean saved = quizService.updateByAdmin(dto);
        if (!saved) {
            return Result.error("-1", "题目信息保存失败");
        }

        return Result.success();
    }

    @PostMapping("/delete")
    public Result<Object> delete(@RequestParam("questionId")
                                 @NotNull(message = "参数异常")
                                 Long questionId) {
        LambdaUpdateWrapper<Quiz> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Quiz::getIsDeleted, 1).eq(Quiz::getId, questionId).eq(Quiz::getIsDeleted, 0);

        boolean deleted = quizService.update(wrapper);
        if (!deleted) {
            return Result.error("-1", "题目信息删除失败");
        }

        return Result.success();
    }

    @Transactional
    @PostMapping("/deleteBatch")
    public Result<Object> deleteBatch(@RequestBody List<Long> questionIds) {
        if (questionIds == null || questionIds.isEmpty()) throw new CustomException("参数异常");

        LambdaUpdateWrapper<Quiz> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Quiz::getIsDeleted, 1).in(Quiz::getId, questionIds);

        boolean deleted = quizService.update(wrapper);
        if (!deleted) {
            return Result.error("-1", "批量题目信息删除失败");
        }

        return Result.success();
    }
}
