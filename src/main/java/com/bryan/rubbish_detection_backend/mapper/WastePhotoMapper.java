package com.bryan.rubbish_detection_backend.mapper;

import com.bryan.rubbish_detection_backend.entity.WastePhoto;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WastePhotoMapper extends MPJBaseMapper<WastePhoto> {
    WastePhoto selectByWasteId(@Param("wasteId") Long wasteId);
}
