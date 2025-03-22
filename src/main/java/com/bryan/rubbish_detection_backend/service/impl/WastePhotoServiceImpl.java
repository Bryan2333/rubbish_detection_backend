package com.bryan.rubbish_detection_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bryan.rubbish_detection_backend.entity.WastePhoto;
import com.bryan.rubbish_detection_backend.mapper.WastePhotoMapper;
import com.bryan.rubbish_detection_backend.service.WastePhotoService;
import org.springframework.stereotype.Service;

@Service
public class WastePhotoServiceImpl extends ServiceImpl<WastePhotoMapper, WastePhoto> implements WastePhotoService {
}
