package com.bryan.rubbish_detection_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bryan.rubbish_detection_backend.entity.News;
import com.bryan.rubbish_detection_backend.entity.PageResult;
import com.bryan.rubbish_detection_backend.exception.CustomException;
import com.bryan.rubbish_detection_backend.mapper.NewsMapper;
import com.bryan.rubbish_detection_backend.service.NewsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {
    @Override
    public PageResult<News> findByPageByAdmin(Integer pageNum, Integer pageSize, String title) {
        LambdaQueryWrapper<News> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(title), News::getTitle, title);
        queryWrapper.eq(News::getIsDeleted, 0);

        Page<News> newsPage = page(new Page<>(pageNum, pageSize), queryWrapper);

        PageResult<News> pageResult = new PageResult<>();
        pageResult.setTotalPages(newsPage.getPages());
        pageResult.setPageSize(newsPage.getSize());
        pageResult.setCurrentPage(newsPage.getCurrent());
        pageResult.setRecords(newsPage.getRecords());
        pageResult.setTotal(newsPage.getTotal());

        return pageResult;
    }
}
