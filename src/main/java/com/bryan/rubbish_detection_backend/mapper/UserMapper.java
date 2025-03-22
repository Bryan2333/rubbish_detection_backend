package com.bryan.rubbish_detection_backend.mapper;

import com.bryan.rubbish_detection_backend.entity.User;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends MPJBaseMapper<User> {
}
