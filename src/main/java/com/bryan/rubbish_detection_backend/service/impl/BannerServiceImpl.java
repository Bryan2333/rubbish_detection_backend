package com.bryan.rubbish_detection_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bryan.rubbish_detection_backend.entity.Banner;
import com.bryan.rubbish_detection_backend.mapper.BannerMapper;
import com.bryan.rubbish_detection_backend.service.BannerService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {
}
