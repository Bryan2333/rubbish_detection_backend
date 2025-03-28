package com.bryan.rubbish_detection_backend.service.impl;

import cn.dev33.satoken.session.SaSession;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bryan.rubbish_detection_backend.entity.Admin;
import com.bryan.rubbish_detection_backend.entity.PageResult;
import com.bryan.rubbish_detection_backend.entity.dto.LoginDTO;
import com.bryan.rubbish_detection_backend.exception.CustomException;
import com.bryan.rubbish_detection_backend.mapper.AdminMapper;
import com.bryan.rubbish_detection_backend.service.AdminService;
import com.bryan.rubbish_detection_backend.utils.ImageUtil;
import com.bryan.rubbish_detection_backend.utils.StpKit;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Value("${app.static.avatar-dir}")
    private String avatarDir;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Admin doLogin(LoginDTO dto) {
        if (dto == null) throw new CustomException("参数异常");

        Admin admin = lambdaQuery().eq(Admin::getUsername, dto.getUsername()).one();
        if (admin != null && passwordEncoder.matches(dto.getPassword(), admin.getPassword())) {
            return admin;
        }

        return null;
    }

    @Override
    public PageResult<Admin> findByPageByAdmin(Integer pageNum, Integer pageSize, String username) {
        LambdaQueryWrapper<Admin> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StringUtils.hasText(username), Admin::getUsername, username);
        queryWrapper.eq(Admin::getIsDeleted, 0);

        IPage<Admin> page = new Page<>(pageNum, pageSize);
        IPage<Admin> adminPage = page(page, queryWrapper);


        PageResult<Admin> pageResult = new PageResult<>();
        pageResult.setCurrentPage(pageNum);
        pageResult.setPageSize(pageSize);
        pageResult.setRecords(page.getRecords());
        pageResult.setTotal(adminPage.getTotal());

        return pageResult;
    }

    @Override
    public Boolean createAdmin(Admin admin) {
        if (admin == null) throw new CustomException("参数异常");

        Admin existAdmin = lambdaQuery()
                .eq(Admin::getUsername, admin.getUsername())
                .eq(Admin::getIsDeleted, 0)
                .one();
        if (existAdmin != null) {
            throw new CustomException("用户名已存在");
        }

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setIsDeleted(0);

        if (StringUtils.hasText(admin.getAvatar())) {
            try {
                String savedFilename = ImageUtil.saveBase64Image(admin.getAvatar(), avatarDir);
                admin.setAvatar("/static/avatars/" + savedFilename);
            } catch (Exception e) {
                throw new CustomException("头像保存失败");
            }
        }

        return save(admin);
    }

    @Override
    public Admin updateAdmin(Admin admin, Boolean currentOperationIsSuper) {
        if (admin == null) throw new CustomException("参数异常");

        Admin existAdmin = lambdaQuery().eq(Admin::getId, admin.getId()).eq(Admin::getIsDeleted, 0).one();
        if (existAdmin == null) {
            throw new CustomException("用户不存在");
        }

        if (!existAdmin.getId().equals(admin.getId())) {
            throw new CustomException("用户名已存在");
        }

        existAdmin.setUsername(admin.getUsername());
        existAdmin.setAge(admin.getAge());
        existAdmin.setEmail(admin.getEmail());
        existAdmin.setPhone(admin.getPhone());
        existAdmin.setGender(admin.getGender());

        if (!Objects.equals(existAdmin.getIsSuper(), admin.getIsSuper())) {
            if (admin.getIsSuper() != 0 && admin.getIsSuper() != 1) {
                throw new CustomException("参数异常");
            }

            if (Objects.equals(false, currentOperationIsSuper)) {
                throw new CustomException("您无权力修改管理员权限");
            }

            existAdmin.setIsSuper(admin.getIsSuper());
        }

        if (StringUtils.hasText(admin.getPassword()) && currentOperationIsSuper) {
            existAdmin.setPassword(passwordEncoder.encode(admin.getPassword()));
        }

        if (StringUtils.hasText(admin.getAvatar())) {
            try {
                String savedFilename = ImageUtil.saveBase64Image(admin.getAvatar(), avatarDir);
                existAdmin.setAvatar("/static/avatars/" + savedFilename);
            } catch (Exception e) {
                throw new CustomException("头像保存失败");
            }
        }

        if (!updateById(existAdmin)) {
            throw new CustomException("更新管理员信息失败");
        }

        SaSession session = StpKit.ADMIN.getSessionByLoginId(admin.getId());
        if (session != null) {
            session.set("isSuper", admin.getIsSuper() == 1);
        }

        return existAdmin;
    }

    @Override
    public Boolean updatePassword(Long adminId, String oldPassword, String newPassword, String confirmPassword) {
        Admin existAdmin = lambdaQuery()
                .eq(Admin::getId, adminId)
                .eq(Admin::getIsDeleted, 0)
                .one();

        if (existAdmin == null) {
            throw new CustomException("用户不存在");
        }

        if (!passwordEncoder.matches(oldPassword, existAdmin.getPassword())) {
            throw new CustomException("原密码错误");
        }

        if (!Objects.equals(newPassword, confirmPassword)) {
            throw new CustomException("两次密码不一致");
        }

        existAdmin.setPassword(passwordEncoder.encode(newPassword));

        return updateById(existAdmin);
    }
}
