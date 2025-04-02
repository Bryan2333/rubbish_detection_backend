package com.bryan.rubbish_detection_backend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.bryan.rubbish_detection_backend.entity.Result;
import com.bryan.rubbish_detection_backend.service.CollectionService;
import com.bryan.rubbish_detection_backend.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/api/echarts")
@SaCheckLogin(type = "admin")
public class EchartsController {
    @Resource
    private OrderService orderService;

    @Resource
    private CollectionService collectionService;

    @PostMapping("/getOrderCountByWasteType")
    public Result<Object> getOrderCountByWasteType() {
        return Result.success(orderService.getOrderCountByWasteType());
    }

    @PostMapping("/getCollectionCountByWasteType")
    public Result<Object> getCollectionCountByWasteType() {
        List<Map<String, Object>> result = collectionService.getCollectionCountByWasteType();

        return Result.success(result);
    }

    @PostMapping("/getWeeklyOrderCount")
    public Result<Object> getWeeklyOrderCount() {
        Map<String, Object> weeklyOrderCount = orderService.getWeeklyOrderCount();

        return Result.success(weeklyOrderCount);
    }
}
