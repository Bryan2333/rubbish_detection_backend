package com.bryan.rubbish_detection_backend.service.impl;

import com.bryan.rubbish_detection_backend.entity.RubbishTypeDesc;
import com.bryan.rubbish_detection_backend.mapper.RubbishTypeDescMapper;
import com.bryan.rubbish_detection_backend.service.RubbishTypeDescService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RubbishTypeDescServiceImpl implements RubbishTypeDescService {
    @Resource
    private RubbishTypeDescMapper mapper;

    @Override
    public RubbishTypeDesc getCompleteInfoByType(Integer type) {
        return mapper.getCompleteInfoByType(type);
    }
}
