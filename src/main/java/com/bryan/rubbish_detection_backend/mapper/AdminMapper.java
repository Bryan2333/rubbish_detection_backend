package com.bryan.rubbish_detection_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bryan.rubbish_detection_backend.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
}
