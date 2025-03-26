package com.bryan.rubbish_detection_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bryan.rubbish_detection_backend.entity.RubbishTypeDesc;

import java.util.List;

public interface RubbishTypeDescService extends IService<RubbishTypeDesc> {
    RubbishTypeDesc getCompleteInfoByType(Integer type);

    List<RubbishTypeDesc> findByList(String name);

    Boolean saveByAdmin(RubbishTypeDesc rubbishTypeDesc);

    Boolean updateByAdmin(RubbishTypeDesc rubbishTypeDesc);
}
