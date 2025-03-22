package com.bryan.rubbish_detection_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bryan.rubbish_detection_backend.entity.Waste;
import com.bryan.rubbish_detection_backend.mapper.WasteMapper;
import com.bryan.rubbish_detection_backend.service.WasteService;
import org.springframework.stereotype.Service;

@Service
public class WasteServiceImpl extends ServiceImpl<WasteMapper, Waste> implements WasteService {
}
