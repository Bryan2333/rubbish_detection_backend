package com.bryan.rubbish_detection_backend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bryan.rubbish_detection_backend.entity.Order;
import com.bryan.rubbish_detection_backend.entity.PageResult;
import com.bryan.rubbish_detection_backend.entity.Result;
import com.bryan.rubbish_detection_backend.entity.User;
import com.bryan.rubbish_detection_backend.entity.dto.OrderDTO;
import com.bryan.rubbish_detection_backend.service.OrderService;
import com.bryan.rubbish_detection_backend.service.UserService;
import com.bryan.rubbish_detection_backend.utils.StpKit;
import com.bryan.rubbish_detection_backend.validator.ValidationGroups;
import com.bryan.rubbish_detection_backend.websocket.WebSocketNotifier;
import jakarta.annotation.Resource;
import jakarta.validation.groups.Default;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/order")
@SaCheckLogin(type = "admin")
public class AdminOrderController {
    @Resource
    private OrderService orderService;

    @Resource
    private WebSocketNotifier webSocketNotifier;

    @PostMapping("/findByPage")
    public Result<PageResult<OrderDTO>> findByPage(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                   @RequestParam(value = "username", required = false) String username,
                                                   @RequestParam(value = "status", required = false) Integer status) {
        if (pageNum < 1 || pageSize < 1) {
            return Result.error("-1", "参数异常");
        }

        if (username != null && username.length() > 20) {
            return Result.error("-1", "参数异常");
        }

        PageResult<OrderDTO> pageResult = orderService.findByPage(null, username, status, pageNum, pageSize);

        return Result.success(pageResult);
    }

    @Transactional
    @PostMapping("/save")
    public Result<OrderDTO> save(@Validated({Default.class, ValidationGroups.FromAdmin.class, ValidationGroups.Create.class}) @RequestBody OrderDTO orderDTO) {
        if (orderDTO == null) {
            return Result.error("-1", "参数异常");
        }

        OrderDTO savedDTO = orderService.saveOrderByAdmin(orderDTO);
        if (savedDTO == null) {
            return Result.error("-1", "保存订单信息失败");
        }

        return Result.success(savedDTO);
    }

    @Transactional
    @PostMapping("/update")
    public Result<Object> update(@Validated({Default.class, ValidationGroups.FromAdmin.class, ValidationGroups.Update.class}) @RequestBody OrderDTO orderDTO) {
        if (orderDTO == null) {
            return Result.error("-1", "参数异常");
        }

        Map<String, Object> map = orderService.updateByAdmin(orderDTO);
        OrderDTO updatedDTO = (OrderDTO) map.get("order");
        User updatedUser = (User) map.get("user");

        if (updatedDTO == null) {
            return Result.error("-1", "更新订单信息失败");
        }

        if (updatedDTO.getOrderStatus() == 2) {
            webSocketNotifier.notifyUserUpdate(updatedDTO.getUserId(), updatedUser);
        }

        return Result.success(orderDTO);
    }

    @Transactional
    @PostMapping("/delete")
    public Result<Object> delete(@RequestParam("orderId") Long orderId) {
        if (orderId == null) {
            return Result.error("-1", "参数异常");
        }

        LambdaUpdateWrapper<Order> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Order::getIsDeleted, 1).eq(Order::getId, orderId).eq(Order::getIsDeleted, 0);

        boolean removed = orderService.update(wrapper);
        if (!removed) {
            return Result.error("-1", "删除订单信息失败");
        }

        return Result.success();
    }

    @Transactional
    @PostMapping("/deleteBatch")
    public Result<Object> deleteBatch(@RequestBody List<Long> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) {
            return Result.error("-1", "参数异常");
        }

        LambdaUpdateWrapper<Order> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Order::getIsDeleted, 1).in(Order::getId, orderIds).eq(Order::getIsDeleted, 0);

        boolean removed = orderService.update(wrapper);
        if (!removed) {
            return Result.error("-1", "批量删除订单信息失败");
        }

        return Result.success();
    }

}
