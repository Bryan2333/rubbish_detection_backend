package com.bryan.rubbish_detection_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bryan.rubbish_detection_backend.entity.PageResult;
import com.bryan.rubbish_detection_backend.entity.RecognitionCollection;

import java.util.List;

public interface CollectionService extends IService<RecognitionCollection> {
    Boolean saveByUser(RecognitionCollection dto);

    Boolean saveByAdmin(RecognitionCollection dto);

    List<RecognitionCollection> findByPage(Long userId, Integer pageNum, Integer pageSize);

    PageResult<RecognitionCollection> findByPageByAdmin(String username, Integer pageNum, Integer pageSize);

    Boolean updateByAdmin(RecognitionCollection dto);
}
