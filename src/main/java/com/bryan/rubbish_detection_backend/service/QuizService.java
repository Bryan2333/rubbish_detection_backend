package com.bryan.rubbish_detection_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bryan.rubbish_detection_backend.entity.PageResult;
import com.bryan.rubbish_detection_backend.entity.Quiz;

public interface QuizService  extends IService<Quiz> {
    PageResult<Quiz> findByPageByAdmin(String question, Integer pageNum, Integer pageSize);

    Boolean saveByAdmin(Quiz dto);

    Boolean updateByAdmin(Quiz dto);
}
