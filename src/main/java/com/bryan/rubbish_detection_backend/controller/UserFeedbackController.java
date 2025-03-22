package com.bryan.rubbish_detection_backend.controller;

import com.bryan.rubbish_detection_backend.entity.Feedback;
import com.bryan.rubbish_detection_backend.entity.Result;
import com.bryan.rubbish_detection_backend.exception.CustomException;
import com.bryan.rubbish_detection_backend.service.FeedbackService;
import com.bryan.rubbish_detection_backend.websocket.WebSocketNotifier;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feedback")
public class UserFeedbackController {
    @Resource
    private FeedbackService feedbackService;

    @Resource
    private WebSocketNotifier webSocketNotifier;

    @PostMapping("/add")
    public Result<Boolean> addFeedback(@Validated({Default.class}) @RequestBody Feedback feedback) {

        boolean submitted = feedbackService.save(feedback);
        if (!submitted) {
            throw new CustomException("反馈提交失败");
        }

        webSocketNotifier.notifyAdminFeedbackUpdate(feedback);

        return Result.success(true);
    }
}
