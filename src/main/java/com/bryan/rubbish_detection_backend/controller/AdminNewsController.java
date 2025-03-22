package com.bryan.rubbish_detection_backend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bryan.rubbish_detection_backend.entity.News;
import com.bryan.rubbish_detection_backend.entity.PageResult;
import com.bryan.rubbish_detection_backend.entity.Result;
import com.bryan.rubbish_detection_backend.service.NewsService;
import com.bryan.rubbish_detection_backend.validator.ValidationGroups;
import jakarta.annotation.Resource;
import jakarta.validation.groups.Default;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/news")
@SaCheckLogin(type = "admin")
public class AdminNewsController {
    @Resource
    private NewsService newsService;

    @PostMapping("/findByPage")
    public Result<PageResult<News>> findByPage(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                               @RequestParam(value = "title", required = false) String title) {
        if (pageNum < 1 || pageSize < 0) {
            return Result.error("-1", "参数异常");
        }

        PageResult<News> pageResult = newsService.findByPageByAdmin(pageNum, pageSize, title);

        return Result.success(pageResult);
    }

    @PostMapping("/save")
    public Result<Object> save(@Validated({Default.class}) @RequestBody News news) {
        boolean saved = newsService.save(news);
        if (!saved) {
            return Result.error("-1", "保存新闻信息");
        }

        return Result.success();
    }

    @PostMapping("/update")
    public Result<Object> update(@Validated({Default.class, ValidationGroups.Update.class}) @RequestBody News news) {
        boolean updated = newsService.updateById(news);
        if (!updated) {
            return Result.error("-1", "更新新闻信息");
        }

        return Result.success();
    }

    @PostMapping("/delete")
    public Result<Object> delete(@RequestParam("newsId") Long newsId) {
        if (newsId == null || newsId < 0) {
            return Result.error("-1", "参数异常");
        }

        LambdaUpdateWrapper<News> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(News::getIsDeleted, 1)
                .eq(News::getId, newsId)
                .eq(News::getIsDeleted, 0);

        boolean updated = newsService.update(wrapper);
        if (!updated) {
            return Result.error("-1", "删除新闻信息失败");
        }

        return Result.success();
    }

    @PostMapping("/deleteBatch")
    public Result<Object> deleteBatch(@RequestBody List<Long> newsIds) {
        if (newsIds == null || newsIds.isEmpty()) {
            return Result.error("-1", "参数异常");
        }

        LambdaUpdateWrapper<News> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(News::getIsDeleted, 1)
                .eq(News::getIsDeleted, 0)
                .in(News::getId, newsIds);

        boolean updated = newsService.update(wrapper);
        if (!updated) {
            return Result.error("-1", "批量删除新闻信息失败");
        }

        return Result.success();
    }
}
