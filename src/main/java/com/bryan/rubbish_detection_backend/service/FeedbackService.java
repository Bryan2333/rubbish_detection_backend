package com.bryan.rubbish_detection_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bryan.rubbish_detection_backend.entity.Feedback;
import com.bryan.rubbish_detection_backend.entity.PageResult;

public interface FeedbackService extends IService<Feedback> {
    PageResult<Feedback> findByPageByAdmin(String name, Integer pageNum, Integer pageSize);

    boolean saveByAdmin(Feedback dto);

    boolean updateByAdmin(Feedback dto);
}
