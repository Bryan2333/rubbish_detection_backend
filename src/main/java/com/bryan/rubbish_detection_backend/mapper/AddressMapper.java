package com.bryan.rubbish_detection_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bryan.rubbish_detection_backend.entity.Address;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressMapper extends MPJBaseMapper<Address> {
}
