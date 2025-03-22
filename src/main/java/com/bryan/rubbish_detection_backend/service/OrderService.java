package com.bryan.rubbish_detection_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bryan.rubbish_detection_backend.entity.Order;
import com.bryan.rubbish_detection_backend.entity.PageResult;
import com.bryan.rubbish_detection_backend.entity.dto.OrderDTO;
import org.aspectj.weaver.ast.Or;

import java.util.List;
import java.util.Map;

public interface OrderService extends IService<Order> {
    PageResult<OrderDTO> findByPage(Long userId, String username, Integer orderStatus, Integer pageNum, Integer pageSize);

    OrderDTO saveOrderByUser(OrderDTO orderDTO);

    OrderDTO saveOrderByAdmin(OrderDTO orderDTO);

    Map<String, Object> updateByAdmin(OrderDTO orderDTO);

    List<OrderDTO> getRecentOrder(Long userId);
}
