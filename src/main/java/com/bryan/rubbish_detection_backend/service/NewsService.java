package com.bryan.rubbish_detection_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bryan.rubbish_detection_backend.entity.News;
import com.bryan.rubbish_detection_backend.entity.PageResult;

public interface NewsService extends IService<News> {
    PageResult<News> findByPageByAdmin(Integer pageNum, Integer pageSize, String title);
}
