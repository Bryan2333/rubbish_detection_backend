package com.bryan.rubbish_detection_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bryan.rubbish_detection_backend.entity.Quiz;
import com.bryan.rubbish_detection_backend.entity.Result;
import com.bryan.rubbish_detection_backend.service.QuizService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
public class UserQuizController {
    @Resource
    private QuizService quizService;

    @GetMapping("/random")
    public Result<List<Quiz>> getRandomQuizzes() {
        LambdaQueryWrapper<Quiz> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Quiz::getIsDeleted, 0);
        queryWrapper.last("ORDER BY RAND() LIMIT 3");
        return Result.success(quizService.list(queryWrapper));
    }
}
