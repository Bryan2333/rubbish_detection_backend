package com.bryan.rubbish_detection_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bryan.rubbish_detection_backend.entity.*;
import com.bryan.rubbish_detection_backend.entity.dto.OrderDTO;
import com.bryan.rubbish_detection_backend.exception.CustomException;
import com.bryan.rubbish_detection_backend.mapper.*;
import com.bryan.rubbish_detection_backend.service.OrderService;
import com.bryan.rubbish_detection_backend.utils.ImageUtil;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private AddressMapper addressMapper;
    @Resource
    private WasteMapper wasteMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private WastePhotoMapper wastePhotoMapper;

    @Value("${app.static.waste-image-dir}")
    private String wasteImageDir;

    private static final String WASTE_PHOTO_NEED_DELETE_FLAG = "needDelete";

    private static final String WASTE_PHOTO_PATH_PREFIX = "/static/waste_images/";

    public PageResult<OrderDTO> findByPage(Long userId, String username, Integer orderStatus, Integer pageNum, Integer pageSize) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(username), "u.username", username);
        queryWrapper.eq(userId != null, "o.user_id", userId);
        queryWrapper.eq(orderStatus != null, "o.order_status", orderStatus);
        queryWrapper.eq("o.is_deleted", 0);
        queryWrapper.orderByDesc("o.created_at");

        IPage<Order> page = orderMapper.findByPage(queryWrapper, new Page<>(pageNum, pageSize));

        PageResult<OrderDTO> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        pageResult.setTotalPages(page.getPages());
        pageResult.setCurrentPage(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        pageResult.setRecords(page.getRecords().stream().map(this::orderToDTO).toList());

        return pageResult;
    }

    public OrderDTO saveOrderByUser(OrderDTO orderDTO) {
        if (orderDTO == null) throw new CustomException("参数异常");

        Long userId = orderDTO.getUserId();

        LambdaQueryWrapper<User> userExistQuery = new LambdaQueryWrapper<>();
        userExistQuery.eq(User::getId, userId).eq(User::getIsDeleted, 0);
        if (null == userMapper.selectOne(userExistQuery)) {
            throw new CustomException("用户不存在");
        }

        return saveOrUpdate(orderDTO);
    }

    public OrderDTO saveOrderByAdmin(OrderDTO orderDTO) {
        if (orderDTO == null) throw new CustomException("参数异常");

        String username = orderDTO.getUsername();

        LambdaQueryWrapper<User> userExistQuery = new LambdaQueryWrapper<>();
        userExistQuery.eq(User::getUsername, username).eq(User::getIsDeleted, 0);
        User dbUser = userMapper.selectOne(userExistQuery);
        if (null == dbUser) {
            throw new CustomException("用户不存在");
        }

        orderDTO.setUserId(dbUser.getId());

        return saveOrUpdate(orderDTO);
    }

    @Override
    public Map<String, Object> updateByAdmin(OrderDTO orderDTO) {
        if (orderDTO == null) {
            throw new CustomException("参数异常");
        }

        LambdaQueryWrapper<User> userExistQuery = new LambdaQueryWrapper<>();
        userExistQuery.eq(User::getId, orderDTO.getUserId()).eq(User::getUsername, orderDTO.getUsername()).eq(User::getIsDeleted, 0);
        User dbUser = userMapper.selectOne(userExistQuery);
        if (null == dbUser) {
            throw new CustomException("用户不存在");
        }

        LambdaQueryWrapper<Order> orderExistQuery = new LambdaQueryWrapper<>();
        orderExistQuery.eq(Order::getId, orderDTO.getId()).eq(Order::getUserId, orderDTO.getUserId()).eq(Order::getIsDeleted, 0);
        if (null == orderMapper.selectOne(orderExistQuery)) {
            throw new CustomException("订单不存在");
        }

        OrderDTO updatedDTO = saveOrUpdate(orderDTO);

        // 如果订单已完成，更新用户的参与次数和总回收金额
        if (updatedDTO.getOrderStatus() == 2) {
            dbUser.setParticipationCount(dbUser.getParticipationCount() + 1);
            dbUser.setTotalRecycleAmount(dbUser.getTotalRecycleAmount().add(updatedDTO.getActualPrice()));
            userMapper.updateById(dbUser);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("user", dbUser);
        map.put("order", updatedDTO);

        return map;
    }

    @Override
    public List<OrderDTO> getRecentOrder(Long userId) {
        if (userId == null) {
            throw new CustomException("用户ID不能为空");
        }

        LambdaQueryWrapper<User> userExistQuery = Wrappers.lambdaQuery();
        userExistQuery.eq(User::getId, userId);
        if (userMapper.selectCount(userExistQuery) <= 0) {
            throw new CustomException("用户不存在");
        }

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("o.user_id", userId);
        queryWrapper.gt("o.order_date", LocalDateTime.now().minusDays(3));
        queryWrapper.lt("o.order_date", LocalDateTime.now().plusDays(3));
        queryWrapper.eq("o.user_id", userId);
        queryWrapper.eq("o.is_deleted", 0);

        List<Order> recentOrder = orderMapper.getRecentOrder(queryWrapper);

        return recentOrder.stream().map(this::orderToDTO).toList();
    }

    private @NotNull OrderDTO orderToDTO(Order dto) {
        OrderDTO orderDTO = new OrderDTO();

        BeanUtils.copyProperties(dto, orderDTO);

        return orderDTO;
    }

    private @NotNull OrderDTO saveOrUpdate(OrderDTO orderDTO) {
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);

        //处理地址
        Address address = new Address();
        BeanUtils.copyProperties(orderDTO.getAddress(), address);
        if (!addressMapper.insertOrUpdate(address)) {
            throw new CustomException("地址信息保存失败");
        }
        order.setAddressId(address.getId());
        order.setAddress(address);

        // 处理垃圾信息
        Waste waste = new Waste();
        BeanUtils.copyProperties(orderDTO.getWaste(), waste);
        if (!wasteMapper.insertOrUpdate(waste)) {
            throw new CustomException("垃圾信息保存失败");
        }
        Long wasteId = waste.getId();
        order.setWasteId(wasteId);
        order.setWaste(waste);

        // 处理垃圾图片
        List<WastePhoto> processWastePhotos = processWastePhotos(orderDTO.getWaste().getPhotos(), wasteId);
        waste.setPhotos(processWastePhotos);

        if (!orderMapper.insertOrUpdate(order)) {
            throw new CustomException("订单信息保存失败");
        }

        return orderToDTO(order);
    }

    // 独立处理垃圾图片
    @Contract("null, _ -> new")
    private @NotNull List<WastePhoto> processWastePhotos(List<WastePhoto> photoDTOs, Long wasteId) {
        if (photoDTOs == null) {
            return new ArrayList<>();
        }

        List<WastePhoto> result = new ArrayList<>();
        for (WastePhoto wp : photoDTOs) {
            String image = wp.getImagePath();
            if (!StringUtils.hasText(image)) {
                continue;
            }

            if (Objects.equals(WASTE_PHOTO_NEED_DELETE_FLAG, image) && wp.getId() != null) {
                int removed = wastePhotoMapper.update(Wrappers.lambdaUpdate(WastePhoto.class)
                        .set(WastePhoto::getIsDeleted, 1)
                        .eq(WastePhoto::getId, wp.getId())
                        .eq(WastePhoto::getIsDeleted, 0));
                if (removed < 1) {
                    throw new CustomException("垃圾图片删除失败");
                }
            } else {
                String savedFilename;
                try {
                    savedFilename = ImageUtil.saveBase64Image(image, wasteImageDir);
                } catch (Exception e) {
                    throw new CustomException("垃圾图片保存失败");
                }
                wp.setWasteId(wasteId);
                wp.setImagePath(WASTE_PHOTO_PATH_PREFIX + savedFilename);
                if (!wastePhotoMapper.insertOrUpdate(wp)) {
                    throw new CustomException("垃圾图片保存失败");
                }
                result.add(wp);
            }
        }
        return result;
    }
}
