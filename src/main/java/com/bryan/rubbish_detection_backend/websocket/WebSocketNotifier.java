package com.bryan.rubbish_detection_backend.websocket;

import com.bryan.rubbish_detection_backend.utils.StpKit;
import jakarta.annotation.Resource;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketNotifier {
    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    public void notifyUserUpdate(Long userId, Object updatedUser) {
        String userToken = StpKit.USER.getTokenValueByLoginId(userId);
        if (userToken != null) {
            simpMessagingTemplate.convertAndSend("/topic/user/" + userToken, updatedUser);
        }
    }

    public void notifyAdminOrderUpdate(Object updatedOrder) {
        simpMessagingTemplate.convertAndSend("/topic/admin/order", updatedOrder);
    }

    public void notifyAdminFeedbackUpdate(Object updatedFeedback) {
        simpMessagingTemplate.convertAndSend("/topic/admin/feedback", updatedFeedback);
    }
}
