package com.bryan.rubbish_detection_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bryan.rubbish_detection_backend.entity.Waste;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WasteMapper extends MPJBaseMapper<Waste> {
}
