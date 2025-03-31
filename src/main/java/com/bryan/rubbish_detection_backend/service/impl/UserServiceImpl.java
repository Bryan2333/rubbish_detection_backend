package com.bryan.rubbish_detection_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bryan.rubbish_detection_backend.entity.PageResult;
import com.bryan.rubbish_detection_backend.entity.User;
import com.bryan.rubbish_detection_backend.entity.dto.*;
import com.bryan.rubbish_detection_backend.exception.CustomException;
import com.bryan.rubbish_detection_backend.mapper.UserMapper;
import com.bryan.rubbish_detection_backend.service.UserService;
import com.bryan.rubbish_detection_backend.utils.ImageUtil;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Value("${app.static.avatar-dir}")
    private String avatarDir;

    @Value("${app.static.verify-code-prefix}")
    private String VERIFY_CODE_PREFIX;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    public Boolean createUser(RegistrationDTO dto) {
        if (dto == null) throw new CustomException("参数异常");

        checkUserExistence(dto.getUsername(), dto.getEmail());

        String verifyCodeKey = VERIFY_CODE_PREFIX + dto.serviceType.getType() + ":" + dto.getEmail();
        validateVerificationCode(verifyCodeKey, dto.getVerifyCode());

        User newUser = buildUser(dto);

        boolean saved = save(newUser);
        if (saved) {
            stringRedisTemplate.delete(verifyCodeKey);
        }

        return saved;
    }

    public Boolean createUserByAdmin(AdminUserDTO dto) {
        if (dto == null) throw new CustomException("参数异常");

        checkUserExistence(dto.getUsername(), dto.getEmail());

        User newUser = buildUser(dto);

        return save(newUser);
    }


    public User doLogin(LoginDTO dto) {
        if (dto == null) throw new CustomException("参数异常");

        User user = lambdaQuery()
                .eq(User::getUsername, dto.getUsername())
                .eq(User::getIsDeleted, 0)
                .one();

        if (user != null && passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return user;
        }

        return null;
    }

    @Override
    public PageResult<UserDTO> findByPageByAdmin(Integer pageNum, Integer pageSize, String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(username), User::getUsername, username);
        queryWrapper.eq(User::getIsDeleted, 0);

        IPage<User> page = new Page<>(pageNum, pageSize);
        IPage<User> userPage = page(page, queryWrapper);

        List<UserDTO> records = userPage.getRecords().stream().map((user) -> {
            UserDTO dto = new UserDTO();
            BeanUtils.copyProperties(user, dto);
            return dto;
        }).toList();

        PageResult<UserDTO> pageResult = new PageResult<>();
        pageResult.setCurrentPage(pageNum);
        pageResult.setPageSize(pageSize);
        pageResult.setRecords(records);
        pageResult.setTotal(userPage.getTotal());

        return pageResult;
    }

    @Override
    public UserDTO updateInfo(UserDTO dto) {
        if (dto == null) throw new CustomException("参数异常");

        return updateUserDetails(dto);
    }

    @Override
    public UserDTO updateInfoByAdmin(UserDTO dto) {
        if (dto == null) throw new CustomException("参数异常");

        return updateUserDetails(dto);
    }

    @Override
    public Boolean changePassword(ChangePasswordDTO dto) {
        if (dto == null) throw new CustomException("参数异常");

        User existUser = lambdaQuery()
                .eq(User::getId, dto.getUserId())
                .eq(User::getIsDeleted, 0)
                .one();
        if (existUser == null) {
            throw new CustomException("用户不存在");
        }

        if (!passwordEncoder.matches(dto.getOldPassword(), existUser.getPassword())) {
            throw new CustomException("原密码错误");
        }

        if (!dto.isPasswordConfirmed()) {
            throw new CustomException("两次密码不一致");
        }

        String verifyCodeKey = VERIFY_CODE_PREFIX + dto.serviceType.getType() + ":" + dto.getUserId();
        validateVerificationCode(verifyCodeKey, dto.getVerifyCode());

        existUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));

        boolean changed = saveOrUpdate(existUser);

        if (changed) {
            stringRedisTemplate.delete(verifyCodeKey);
        }

        return changed;
    }

    @Override
    public Boolean changeEmail(ChangeEmailDTO dto) {
        if (dto == null) throw new CustomException("参数异常");

        User existUser = lambdaQuery()
                .eq(User::getId, dto.getUserId())
                .eq(User::getIsDeleted, 0)
                .one();
        if (existUser == null) {
            throw new CustomException("用户不存在");
        }

        String verifyCodeKey = VERIFY_CODE_PREFIX + dto.serviceType.getType() + ":" + dto.getUserId();
        validateVerificationCode(verifyCodeKey, dto.getVerifyCode());

        existUser.setEmail(dto.getNewEmail());

        boolean changed = saveOrUpdate(existUser);
        if (changed) {
            stringRedisTemplate.delete(verifyCodeKey);
        }

        return changed;
    }

    public Boolean resetPassword(ResetPasswordDTO dto) {
        if (dto == null) throw new CustomException("参数异常");

        User existUser = lambdaQuery()
                .eq(User::getEmail, dto.getEmail())
                .eq(User::getIsDeleted, 0)
                .one();
        if (existUser == null) {
            throw new CustomException("用户不存在");
        }

        if (!Objects.equals(existUser.getUsername(), dto.getUsername())) {
            throw new CustomException("用户名与邮箱不匹配");
        }

        if (!dto.isPasswordConfirmed()) {
            throw new CustomException("两次密码不一致");
        }

        String verifyCodeKey = VERIFY_CODE_PREFIX + dto.serviceType.getType() + ":" + existUser.getId();
        String verifyCode = stringRedisTemplate.opsForValue().get(verifyCodeKey);
        if (!StringUtils.hasText(verifyCode)) {
            throw new CustomException("验证码已过期，请重新获取");
        }

        if (!Objects.equals(verifyCode, dto.getVerifyCode())) {
            throw new CustomException("验证码错误，请重新输入");
        }

        existUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));

        boolean reset = saveOrUpdate(existUser);
        if (reset) {
            stringRedisTemplate.delete(verifyCodeKey);
        }

        return reset;
    }

    private void checkUserExistence(String username, String email) {
        User existingUser = lambdaQuery()
                .eq(User::getUsername, username)
                .or()
                .eq(User::getEmail, email)
                .and((user) -> user.eq(User::getIsDeleted, 0))
                .one();
        if (existingUser != null) {
            throw new CustomException("用户名或邮箱已被注册");
        }
    }

    private void validateVerificationCode(String verifyCodeKey, String inputCode) {
        String verifyCode = stringRedisTemplate.opsForValue().get(verifyCodeKey);

        if (!StringUtils.hasText(verifyCode)) {
            throw new CustomException("验证码已过期，请重新获取");
        }
        if (!Objects.equals(verifyCode, inputCode)) {
            throw new CustomException("验证码错误，请重新输入");
        }
    }

    @Contract("null -> fail")
    private @NotNull User buildUser(UserDTO dto) {
        if (dto == null) throw new CustomException("参数异常");

        User newUser = new User();
        newUser.setUsername(dto.getUsername());
        newUser.setEmail(dto.getEmail());
        newUser.setAge(dto.getAge());
        newUser.setGender(dto.getGender());
        newUser.setSignature(dto.getSignature());
        newUser.setIsDeleted(0);

        if (dto instanceof RegistrationDTO regDTO) {
            newUser.setParticipationCount(0);
            newUser.setTotalRecycleAmount(new BigDecimal("0.00"));
            newUser.setPassword(passwordEncoder.encode(regDTO.getPassword()));
        }

        if (dto instanceof AdminUserDTO auDTO) {
            newUser.setParticipationCount(auDTO.getParticipationCount());
            newUser.setTotalRecycleAmount(auDTO.getTotalRecycleAmount());
            newUser.setPassword(passwordEncoder.encode(auDTO.getPassword()));
        }

        if (StringUtils.hasText(dto.getAvatar())) {
            try {
                String savedFilename = ImageUtil.saveBase64Image(dto.getAvatar(), avatarDir);
                newUser.setAvatar("/static/avatars/" + savedFilename);
            } catch (Exception e) {
                throw new CustomException("头像保存失败");
            }
        }

        return newUser;
    }

    @Contract("null -> fail")
    private @NotNull UserDTO updateUserDetails(UserDTO dto) {
        if (dto == null) throw new CustomException("参数异常");

        User userExist = lambdaQuery().eq(User::getId, dto.getId()).one();
        if (userExist == null) {
            throw new CustomException("用户不存在");
        }

        // 检查用户名是否被占用
        boolean isUsernameUsed = lambdaQuery()
                .eq(User::getUsername, dto.getUsername())
                .ne(User::getId, dto.getId())
                .one() != null;
        if (isUsernameUsed) {
            throw new CustomException("用户名已被使用");
        }

        userExist.setUsername(dto.getUsername());
        userExist.setAge(dto.getAge());
        userExist.setGender(dto.getGender());
        userExist.setSignature(dto.getSignature());

        // 只有管理员可以修改邮箱和密码
        if (dto instanceof AdminUserDTO auDTO) {
            if (StringUtils.hasText(auDTO.getPassword())) {
                userExist.setPassword(passwordEncoder.encode(auDTO.getPassword()));
            }

            userExist.setEmail(auDTO.getEmail());
            userExist.setTotalRecycleAmount(auDTO.getTotalRecycleAmount());
            userExist.setParticipationCount(auDTO.getParticipationCount());
        }

        if (StringUtils.hasText(dto.getAvatar())) {
            try {
                String savedFilename = ImageUtil.saveBase64Image(dto.getAvatar(), avatarDir);
                userExist.setAvatar("/static/avatars/" + savedFilename);
            } catch (Exception e) {
                throw new CustomException("头像保存失败");
            }
        }

        if (!updateById(userExist)) {
            throw new CustomException("用户信息更新失败");
        }

        UserDTO updatedUserDTO = new UserDTO();
        BeanUtils.copyProperties(userExist, updatedUserDTO);
        return updatedUserDTO;
    }
}
