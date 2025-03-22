package com.bryan.rubbish_detection_backend.service;

import com.bryan.rubbish_detection_backend.entity.RubbishTypeDesc;

public interface RubbishTypeDescService {
    RubbishTypeDesc getCompleteInfoByType(Integer type);
}
