package com.bryan.rubbish_detection_backend.controller;

import com.bryan.rubbish_detection_backend.entity.Banner;
import com.bryan.rubbish_detection_backend.entity.Result;
import com.bryan.rubbish_detection_backend.service.BannerService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/banner")
public class UserBannerController {
    @Resource
    private BannerService bannerService;

    @GetMapping("/getBanner")
    public Result<List<Banner>> getBanner() {
        List<Banner> banners = bannerService.list();
        return Result.success(banners);
    }
}
