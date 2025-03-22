package com.bryan.rubbish_detection_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bryan.rubbish_detection_backend.entity.Feedback;
import com.bryan.rubbish_detection_backend.entity.PageResult;
import com.bryan.rubbish_detection_backend.mapper.FeedbackMapper;
import com.bryan.rubbish_detection_backend.service.FeedbackService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {
    @Override
    public PageResult<Feedback> findByPageByAdmin(String name, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Feedback> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(name), Feedback::getName, name);
        wrapper.eq(Feedback::getIsDeleted, 0);
        wrapper.orderByDesc(Feedback::getId);

        Page<Feedback> page = new Page<>(pageNum, pageSize);
        Page<Feedback> feedBackPage = page(page, wrapper);

        PageResult<Feedback> pageResult = new PageResult<>();
        pageResult.setTotalPages(feedBackPage.getPages());
        pageResult.setCurrentPage(feedBackPage.getCurrent());
        pageResult.setPageSize(feedBackPage.getSize());
        pageResult.setTotal(feedBackPage.getTotal());
        pageResult.setRecords(feedBackPage.getRecords());


        return pageResult;
    }

    @Override
    public boolean saveByAdmin(Feedback dto) {
        return saveFeedBack(dto);
    }

    @Override
    public boolean updateByAdmin(Feedback dto) {
        return saveFeedBack(dto);
    }

    private boolean saveFeedBack(Feedback dto) {
        if (dto == null) return false;

        return saveOrUpdate(dto);
    }
}
