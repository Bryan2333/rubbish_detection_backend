package com.bryan.rubbish_detection_backend.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bryan.rubbish_detection_backend.entity.Order;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
@CacheNamespace
public interface OrderMapper extends BaseMapper<Order> {
    IPage<Order> findByPage(@Param(Constants.WRAPPER) QueryWrapper<Order> wrapper, IPage<Order> page);

    List<Order> getRecentOrder(@Param(Constants.WRAPPER) QueryWrapper<Order> wrapper);

    List<Map<String, Object>> getOrderCountByWasteType();
}
