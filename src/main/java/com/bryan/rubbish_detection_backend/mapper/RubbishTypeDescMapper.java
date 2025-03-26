package com.bryan.rubbish_detection_backend.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bryan.rubbish_detection_backend.entity.RubbishTypeDesc;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@CacheNamespace
public interface RubbishTypeDescMapper extends BaseMapper<RubbishTypeDesc> {
    RubbishTypeDesc getCompleteInfoByType(@Param(Constants.WRAPPER) QueryWrapper<RubbishTypeDesc> wrapper);

    List<RubbishTypeDesc> findByList(@Param(Constants.WRAPPER) QueryWrapper<RubbishTypeDesc> wrapper);
}
