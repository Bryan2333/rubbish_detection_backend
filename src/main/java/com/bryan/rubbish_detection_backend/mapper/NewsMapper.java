package com.bryan.rubbish_detection_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bryan.rubbish_detection_backend.entity.News;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@CacheNamespace
public interface NewsMapper extends BaseMapper<News> {
}

