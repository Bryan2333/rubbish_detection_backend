package com.bryan.rubbish_detection_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bryan.rubbish_detection_backend.entity.RubbishTypeCommonThing;
import com.bryan.rubbish_detection_backend.entity.RubbishTypeDesc;
import com.bryan.rubbish_detection_backend.entity.RubbishTypeDisposalAdvice;
import com.bryan.rubbish_detection_backend.entity.RubbishTypeHandleMethod;
import com.bryan.rubbish_detection_backend.exception.CustomException;
import com.bryan.rubbish_detection_backend.mapper.RubbishTypeCommonThingMapper;
import com.bryan.rubbish_detection_backend.mapper.RubbishTypeDescMapper;
import com.bryan.rubbish_detection_backend.mapper.RubbishTypeDisposalAdviceMapper;
import com.bryan.rubbish_detection_backend.mapper.RubbishTypeHandleMethodMapper;
import com.bryan.rubbish_detection_backend.service.RubbishTypeDescService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class RubbishTypeDescServiceImpl extends ServiceImpl<RubbishTypeDescMapper, RubbishTypeDesc> implements RubbishTypeDescService {
    @Resource
    private RubbishTypeDescMapper descMapper;
    @Resource
    private RubbishTypeDisposalAdviceMapper disposalAdviceMapper;
    @Resource
    private RubbishTypeHandleMethodMapper handleMethodMapper;
    @Resource
    private RubbishTypeCommonThingMapper commonThingMapper;

    @Override
    public RubbishTypeDesc getCompleteInfoByType(Integer type) {
        QueryWrapper<RubbishTypeDesc> wrapper = new QueryWrapper<>();
        wrapper.eq("r.type", type);
        wrapper.like("r.is_deleted", 0);
        return descMapper.getCompleteInfoByType(wrapper);
    }

    @Override
    public List<RubbishTypeDesc> findByList(String name) {
        QueryWrapper<RubbishTypeDesc> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.hasText(name), "r.name", name);
        wrapper.like("r.is_deleted", 0);

        return descMapper.findByList(wrapper);
    }

    @Override
    public Boolean saveByAdmin(RubbishTypeDesc rubbishTypeDesc) {
        if (rubbishTypeDesc == null) throw new CustomException("参数异常");

        int insert = descMapper.insert(rubbishTypeDesc);
        if (insert < 0) {
            throw new CustomException("垃圾分类指南信息保存失败");
        }

        processItems(rubbishTypeDesc.getDisposalAdvice(), rubbishTypeDesc.getId(), false);
        processItems(rubbishTypeDesc.getHandleMethods(), rubbishTypeDesc.getId(), false);
        processItems(rubbishTypeDesc.getCommonThings(), rubbishTypeDesc.getId(), false);

        return true;
    }

    @Override
    public Boolean updateByAdmin(RubbishTypeDesc rubbishTypeDesc) {
        if (rubbishTypeDesc == null) throw new CustomException("参数异常");

        int insert = descMapper.updateById(rubbishTypeDesc);
        if (insert < 0) {
            throw new CustomException("垃圾分类指南信息保存失败");
        }

        processItems(rubbishTypeDesc.getDisposalAdvice(), rubbishTypeDesc.getId(), true);
        processItems(rubbishTypeDesc.getHandleMethods(), rubbishTypeDesc.getId(), true);
        processItems(rubbishTypeDesc.getCommonThings(), rubbishTypeDesc.getId(), true);

        return true;
    }

    private <T> void processItems(List<T> items, Integer rubbishId, boolean isUpdate) {
        if (items == null || items.isEmpty()) {
            throw new CustomException("内容不能为空");
        }

        for (T item : items) {
            if (item instanceof RubbishTypeDisposalAdvice advice) {
                if (!StringUtils.hasText(advice.getAdvice())) {
                    throw new CustomException("垃圾投放建议内容不能为空");
                }
                advice.setRubbishId(rubbishId);
                int result = isUpdate ? disposalAdviceMapper.updateById(advice) : disposalAdviceMapper.insert(advice);
                if (result < 0) {
                    throw new CustomException("垃圾投放建议保存失败");
                }
            } else if (item instanceof RubbishTypeHandleMethod method) {
                if (!StringUtils.hasText(method.getMethod())) {
                    throw new CustomException("垃圾处理方法内容不能为空");
                }
                method.setRubbishId(rubbishId);
                int result = isUpdate ? handleMethodMapper.updateById(method) : handleMethodMapper.insert(method);
                if (result < 0) {
                    throw new CustomException("垃圾处理方法保存失败");
                }
            } else if (item instanceof RubbishTypeCommonThing thing) {
                if (!StringUtils.hasText(thing.getThing())) {
                    throw new CustomException("常见物品内容不能为空");
                }
                thing.setRubbishId(rubbishId);
                int result = isUpdate ? commonThingMapper.updateById(thing) : commonThingMapper.insert(thing);
                if (result < 0) {
                    throw new CustomException("常见物品保存失败");
                }
            }
        }
    }
}
