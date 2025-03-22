package com.bryan.rubbish_detection_backend.controller;

import com.bryan.rubbish_detection_backend.entity.Result;
import com.bryan.rubbish_detection_backend.entity.RubbishTypeDesc;
import com.bryan.rubbish_detection_backend.service.RubbishTypeDescService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rubbish-type")
public class RubbishTypeDescController {
    @Resource
    private RubbishTypeDescService rubbishTypeDescService;

    @GetMapping("/getDesc")
    public Result<RubbishTypeDesc> getCompleteInfo(@RequestParam("type") Integer type) {
        RubbishTypeDesc desc = rubbishTypeDescService.getCompleteInfoByType(type);
        return Result.success(desc);
    }
}
