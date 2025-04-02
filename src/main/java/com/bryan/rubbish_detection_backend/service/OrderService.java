package com.bryan.rubbish_detection_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bryan.rubbish_detection_backend.entity.Order;
import com.bryan.rubbish_detection_backend.entity.PageResult;
import com.bryan.rubbish_detection_backend.entity.dto.OrderDTO;

import java.util.List;
import java.util.Map;

public interface OrderService extends IService<Order> {
    PageResult<OrderDTO> findByPage(Long userId, String username, Integer orderStatus, Integer pageNum, Integer pageSize);

    OrderDTO saveOrderByUser(OrderDTO orderDTO);

    OrderDTO saveOrderByAdmin(OrderDTO orderDTO);

    Map<String, Object> updateByAdmin(OrderDTO orderDTO);

    List<OrderDTO> getRecentOrder(Long userId);

    OrderDTO cancelOrder(Long userId, Long orderId);

    Boolean saveReview(Long userId, Long orderId, Integer reviewRate, String reviewMessage);

    List<Map<String, Object>> getOrderCountByWasteType();

    Map<String, Object> getWeeklyOrderCount();
}
