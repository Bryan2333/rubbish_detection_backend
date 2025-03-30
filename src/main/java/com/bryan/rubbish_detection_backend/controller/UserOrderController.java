package com.bryan.rubbish_detection_backend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.bryan.rubbish_detection_backend.annotation.CheckCurrentUser;
import com.bryan.rubbish_detection_backend.entity.PageResult;
import com.bryan.rubbish_detection_backend.entity.Result;
import com.bryan.rubbish_detection_backend.entity.dto.OrderDTO;
import com.bryan.rubbish_detection_backend.exception.CustomException;
import com.bryan.rubbish_detection_backend.service.OrderService;
import com.bryan.rubbish_detection_backend.validator.ValidationGroups;
import com.bryan.rubbish_detection_backend.websocket.WebSocketNotifier;
import jakarta.annotation.Resource;
import jakarta.validation.groups.Default;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SaCheckLogin(type = "user")
@RestController
@RequestMapping("/api/order")
public class UserOrderController {
    @Resource
    private OrderService orderService;

    @Resource
    private WebSocketNotifier webSocketNotifier;

    @GetMapping("/findByPage")
    @CheckCurrentUser
    public Result<List<OrderDTO>> findByPage(@RequestParam(value = "userId") Long userId,
                                             @RequestParam(value = "orderStatus", required = false) Integer orderStatus,
                                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        if (userId == null) {
            throw new CustomException("用户ID不能为空");
        }

        if (pageNum <= 0) {
            throw new CustomException("页码不能小于等于0");
        }

        if (pageSize <= 0) {
            throw new CustomException("每页数量不能小于等于0");
        }

        PageResult<OrderDTO> pageResult = orderService.findByPage(userId, null, orderStatus, pageNum, pageSize);

        return Result.success(pageResult.getRecords());
    }

    @PostMapping("/add")
    @Transactional
    @CheckCurrentUser
    public Result<OrderDTO> addOrder(@Validated({Default.class, ValidationGroups.FromUser.class, ValidationGroups.Create.class}) @RequestBody OrderDTO orderDTO) {
        OrderDTO result = orderService.saveOrderByUser(orderDTO);

        webSocketNotifier.notifyAdminOrderUpdate(orderDTO);

        return Result.success(result);
    }

    @GetMapping("/getRecent")
    @CheckCurrentUser
    public Result<List<OrderDTO>> getRecentOrder(@RequestParam("userId") Long userId) {
        List<OrderDTO> orders = orderService.getRecentOrder(userId);

        return Result.success(orders);
    }

    @PostMapping("/cancel")
    @CheckCurrentUser
    public Result<Object> cancelOrder(@RequestParam("userId") Long userId,
                                      @RequestParam("orderId") Long orderId) {
        Boolean canceled = orderService.cancelOrder(userId, orderId);
        if (!canceled) {
            return Result.error("-1", "取消回收订单失败");
        }

        return Result.success();
    }

    @PostMapping("/addReview")
    @CheckCurrentUser
    public Result<Object> addReview(@RequestParam("userId") Long userId,
                                    @RequestParam("orderId") Long orderId,
                                    @RequestParam("reviewRate") Integer reviewRate,
                                    @RequestParam("reviewMessage") String reviewMessage) {
        Boolean added = orderService.saveReview(userId, orderId, reviewRate, reviewMessage);

        if (!added) {
            return Result.error("-1", "提交订单评价失败");
        }

        return Result.success();
    }

}
