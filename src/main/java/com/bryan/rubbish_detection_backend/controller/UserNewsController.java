package com.bryan.rubbish_detection_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bryan.rubbish_detection_backend.entity.News;
import com.bryan.rubbish_detection_backend.entity.Result;
import com.bryan.rubbish_detection_backend.service.NewsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class UserNewsController {
    @Resource
    private NewsService newsArticleService;

    @GetMapping("/page")
    public Result<List<News>> findByPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "5") int pageSize) {

        LambdaQueryWrapper<News> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(News::getPublishTime);

        IPage<News> newsArticlePage = newsArticleService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<News> records = newsArticlePage.getRecords();
        return Result.success(records);
    }
}
