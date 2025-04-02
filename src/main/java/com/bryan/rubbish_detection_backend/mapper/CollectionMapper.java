package com.bryan.rubbish_detection_backend.mapper;

import com.bryan.rubbish_detection_backend.entity.RecognitionCollection;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
@CacheNamespace
public interface CollectionMapper extends MPJBaseMapper<RecognitionCollection> {
    List<Map<String, Object>> getCollectionCountByWasteType();
}
