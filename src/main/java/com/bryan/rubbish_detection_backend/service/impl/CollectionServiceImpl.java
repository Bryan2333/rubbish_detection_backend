package com.bryan.rubbish_detection_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bryan.rubbish_detection_backend.entity.PageResult;
import com.bryan.rubbish_detection_backend.entity.RecognitionCollection;
import com.bryan.rubbish_detection_backend.entity.User;
import com.bryan.rubbish_detection_backend.entity.enumeration.WasteTypeEnum;
import com.bryan.rubbish_detection_backend.exception.CustomException;
import com.bryan.rubbish_detection_backend.mapper.CollectionMapper;
import com.bryan.rubbish_detection_backend.mapper.UserMapper;
import com.bryan.rubbish_detection_backend.service.CollectionService;
import com.bryan.rubbish_detection_backend.utils.ImageUtil;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, RecognitionCollection> implements CollectionService {
    @Resource
    private UserMapper userMapper;

    @Resource
    CollectionMapper collectionMapper;

    @Value("${app.static.collection-image-dir}")
    private String collectionImageDir;

    /**
     * 第三方和自己
     * 0  可回收物 -> 2
     * 1  有害垃圾 -> 3
     * 2  湿垃圾 -> 1
     * 3  干垃圾 -> 0
     */
    private final HashMap<Integer, WasteTypeEnum> rubbishTypeMap = new HashMap<>() {{
        put(0, WasteTypeEnum.RECYCLE);
        put(1, WasteTypeEnum.HARMFUL);
        put(2, WasteTypeEnum.WET);
        put(3, WasteTypeEnum.DRY);
    }};

    @Override
    public Boolean saveByUser(RecognitionCollection dto) {
        if (dto == null) throw new CustomException("参数异常");

        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(User::getId, dto.getUserId());
        User dbUser = userMapper.selectOne(queryWrapper);
        if (dbUser == null) {
            throw new CustomException("用户不存在");
        }

        return saveCollection(dbUser, dto);
    }

    @Override
    public Boolean saveByAdmin(RecognitionCollection dto) {
        if (dto == null) throw new CustomException("参数异常");

        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(User::getUsername, dto.getUsername());
        User dbUser = userMapper.selectOne(queryWrapper);
        if (dbUser == null) {
            throw new CustomException("用户不存在");
        }

        return saveCollection(dbUser, dto);
    }

    @Override
    public List<RecognitionCollection> findByPage(Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<User> userWrapper = Wrappers.lambdaQuery();
        userWrapper.eq(User::getId, userId);
        userWrapper.eq(User::getIsDeleted, 0);
        User user = userMapper.selectOne(userWrapper);
        if (user == null) {
            throw new CustomException("用户不存在");
        }

        LambdaQueryWrapper<RecognitionCollection> collectionWrapper = Wrappers.lambdaQuery();
        collectionWrapper.eq(RecognitionCollection::getUserId, userId);
        collectionWrapper.eq(RecognitionCollection::getIsDeleted, 0);

        Page<RecognitionCollection> page = page(new Page<>(pageNum, pageSize), collectionWrapper);

        return page.getRecords();
    }

    @Override
    public PageResult<RecognitionCollection> findByPageByAdmin(String username, Integer pageNum, Integer pageSize) {
        MPJLambdaWrapper<RecognitionCollection> wrapper = JoinWrappers.lambda(RecognitionCollection.class)
                .selectAll()
                .selectAs(User::getUsername, RecognitionCollection::getUsername)
                .leftJoin(User.class, User::getId, RecognitionCollection::getUserId)
                .like(StringUtils.hasText(username), User::getUsername, username)
                .eq(RecognitionCollection::getIsDeleted, 0);


        Page<RecognitionCollection> page = collectionMapper.selectJoinPage(new Page<>(pageNum, pageSize), wrapper);

        PageResult<RecognitionCollection> pageResult = new PageResult<>();
        pageResult.setCurrentPage(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        pageResult.setTotal(page.getTotal());
        pageResult.setTotalPages(page.getPages());
        pageResult.setRecords(page.getRecords());

        return pageResult;
    }

    @Override
    public Boolean updateByAdmin(RecognitionCollection dto) {
        if (dto == null) throw new CustomException("参数异常");

        if (dto.getId() == null || dto.getId() < 0) {
            throw new CustomException("收藏识别ID不合法");
        }

        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(User::getUsername, dto.getUsername());
        queryWrapper.eq(User::getId, dto.getUserId());
        User dbUser = userMapper.selectOne(queryWrapper);
        if (dbUser == null) {
            throw new CustomException("用户不存在");
        }

        return saveCollection(dbUser, dto);
    }

    @Contract("_, null -> fail; null, !null -> fail")
    private @NotNull Boolean saveCollection(User user, RecognitionCollection dto) {
        if (dto == null || user == null) throw new CustomException("参数异常");

        RecognitionCollection recognitionCollection = new RecognitionCollection();
        recognitionCollection.setUserId(user.getId());
        recognitionCollection.setRubbishName(dto.getRubbishName());
        recognitionCollection.setCreatedAt(dto.getCreatedAt());
        recognitionCollection.setIsDeleted(0);

        // 更新
        if (dto.getId() != null) {
            recognitionCollection.setId(dto.getId());
        }

        // 管理员添加，不需要类型映射
        if (!StringUtils.hasText(dto.getUsername())) {
            recognitionCollection.setRubbishType(rubbishTypeMap.get(dto.getRubbishType()));
        } else {
            recognitionCollection.setRubbishType(dto.getRubbishType());
        }

        String image = dto.getImage();
        if (StringUtils.hasText(image)) {
            try {
                String savedFilename = ImageUtil.saveBase64Image(image, collectionImageDir);
                recognitionCollection.setImage("/static/collection_images/" + savedFilename);
            } catch (Exception e) {
                throw new CustomException("保存识别图片失败");
            }
        }

        return saveOrUpdate(recognitionCollection);
    }

}
