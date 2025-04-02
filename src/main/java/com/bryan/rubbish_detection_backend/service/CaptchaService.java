package com.bryan.rubbish_detection_backend.service;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bryan.rubbish_detection_backend.entity.User;
import com.bryan.rubbish_detection_backend.entity.dto.CaptchaRequestDTO;
import com.bryan.rubbish_detection_backend.entity.enumeration.CaptchaServiceTypeEnum;
import com.bryan.rubbish_detection_backend.exception.CustomException;
import com.bryan.rubbish_detection_backend.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class CaptchaService {
    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${app.static.verify-code-prefix}")
    private String VERIFY_CODE_PREFIX;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserMapper userMapper;

    public void sendRegisterVerifyCode(CaptchaRequestDTO.RegisterRequest request) {
        if (request == null) throw new CustomException("参数异常");

        String email = request.getEmail();

        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(User::getEmail, email);
        if (userMapper.selectOne(queryWrapper) != null) {
            throw new CustomException("该邮箱已被使用，请更换邮箱");
        }

        String verifyCode = generateVerifyCode();
        int serviceType = request.serviceType.getType();

        sendVerifyCode(email, verifyCode, serviceType);

        saveCodeToRedis(email, serviceType, verifyCode);
    }

    public void sendChangePasswordVerifyCode(CaptchaRequestDTO.ChangePasswordRequest request) {
        if (request == null) throw new CustomException("参数异常");

        Long userId = request.getUserId();

        User user = userMapper.selectById(userId);
        if (Objects.isNull(user)) {
            throw new CustomException("用户不存在");
        }

        String verifyCode = generateVerifyCode();
        int serviceType = request.serviceType.getType();

        sendVerifyCode(user.getEmail(), verifyCode, serviceType);

        saveCodeToRedis(userId.toString(), serviceType, verifyCode);
    }

    public void sendChangeEmailVerifyCode(CaptchaRequestDTO.ChangeEmailRequest request) {
        if (request == null) throw new CustomException("参数异常");

        Long userId = request.getUserId();
        User user = userMapper.selectById(userId);
        if (Objects.isNull(user)) {
            throw new CustomException("用户不存在");
        }

        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(User::getEmail, request.getNewEmail());
        if (userMapper.selectOne(queryWrapper) != null) {
            throw new CustomException("该邮箱已被使用，请更换邮箱");
        }

        String verifyCode = generateVerifyCode();
        int serviceType = request.serviceType.getType();

        sendVerifyCode(request.getNewEmail(), verifyCode, serviceType);

        saveCodeToRedis(userId.toString(), serviceType, verifyCode);
    }

    public void sendForgetPasswordVerifyCode(CaptchaRequestDTO.ForgetPasswordRequest request) {
        if (request == null) throw new CustomException("参数异常");

        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(User::getUsername, request.getUsername());
        User user = userMapper.selectOne(queryWrapper);
        if (Objects.isNull(user)) {
            throw new CustomException("用户不存在");
        }

        if (!Objects.equals(user.getEmail(), request.getEmail())) {
            throw new CustomException("输入的邮箱和用户绑定的邮箱不一致");
        }

        String verifyCode = generateVerifyCode();
        int serviceType = request.serviceType.getType();

        sendVerifyCode(user.getEmail(), verifyCode, serviceType);

        saveCodeToRedis(user.getId().toString(), serviceType, verifyCode);

    }

    private void saveCodeToRedis(String identity, int serviceType, String verifyCode) {
        stringRedisTemplate.opsForValue().set(VERIFY_CODE_PREFIX + serviceType + ":" + identity, verifyCode, 10, TimeUnit.MINUTES);
    }

    private void sendVerifyCode(String email, String verifyCode, Integer serviceType) {
        String serviceName = CaptchaServiceTypeEnum.getDescription(serviceType);
        String content = getContent(verifyCode, serviceName);
        try {
            sendEmail(email, serviceName + "-验证码", content);
        } catch (Exception e) {
            if (e.getMessage().contains("550")) {
                throw new CustomException("邮箱地址不存在或不可用，请检查邮箱地址");
            } else {
                throw new CustomException("验证码发送失败，请稍后再试");
            }
        }
    }

    private @NotNull String generateVerifyCode() {
        return RandomUtil.randomNumbers(6);
    }

    private void sendEmail(String email, String subject, String content) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(mailUsername);
        mimeMessageHelper.setTo(email);
        mimeMessage.setSubject(subject);
        mimeMessageHelper.setText(content, true);
        mailSender.send(mimeMessage);
    }


    @Contract(pure = true)
    private @NotNull String getContent(String verifyCode, String serviceName) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>%s-验证码</title>
                    <style>
                        body { font-family: sans-serif; line-height: 1.6; }
                        .container { max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px; }
                        .code { font-size: 24px; font-weight: bold; color: #d9534f; }
                        .footer { font-size: 12px; color: #888; margin-top: 20px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h2>%s-验证码</h2>
                        <p>尊敬的用户，</p>
                        <p>您正在%s，请使用以下验证码完成验证：</p>
                        <p class="code">%s</p>
                        <p>该验证码有效期为 <strong>10 分钟</strong>，请尽快完成验证。</p>
                        <p>如果您未发起此操作，请忽略此邮件，无需进一步操作。</p>
                        <div class="footer">
                            <p>感谢您的使用！</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(serviceName, serviceName, serviceName, verifyCode);
    }

}
