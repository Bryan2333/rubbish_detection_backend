package com.bryan.rubbish_detection_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bryan.rubbish_detection_backend.entity.PageResult;
import com.bryan.rubbish_detection_backend.entity.Quiz;
import com.bryan.rubbish_detection_backend.exception.CustomException;
import com.bryan.rubbish_detection_backend.mapper.QuizMapper;
import com.bryan.rubbish_detection_backend.service.QuizService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class QuizServiceImpl extends ServiceImpl<QuizMapper, Quiz> implements QuizService {
    @Override
    public PageResult<Quiz> findByPageByAdmin(String question, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Quiz> queryWrapper = new LambdaQueryWrapper<>();

        if (question != null && question.length() > 50) {
            throw new CustomException("问题长度不能超过50个字符");
        }

        queryWrapper.like(StringUtils.hasText(question), Quiz::getQuestion, question);
        queryWrapper.eq(Quiz::getIsDeleted, 0);

        IPage<Quiz> page = new Page<>(pageNum, pageSize);
        IPage<Quiz> quizPage = page(page, queryWrapper);

        PageResult<Quiz> pageResult = new PageResult<>();
        pageResult.setCurrentPage(quizPage.getCurrent());
        pageResult.setPageSize(quizPage.getSize());
        pageResult.setRecords(quizPage.getRecords());
        pageResult.setTotal(quizPage.getTotal());

        return pageResult;
    }

    @Override
    public Boolean saveByAdmin(Quiz dto) {
        if (dto == null) throw new CustomException("参数异常");

        dto.setIsDeleted(0);

        return save(dto);
    }

    @Override
    public Boolean updateByAdmin(Quiz dto) {
        if (dto == null) throw new CustomException("参数异常");

        return updateById(dto);
    }
}
