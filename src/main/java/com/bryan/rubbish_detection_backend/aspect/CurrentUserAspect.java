package com.bryan.rubbish_detection_backend.aspect;

import com.bryan.rubbish_detection_backend.annotation.CheckCurrentUser;
import com.bryan.rubbish_detection_backend.exception.CustomException;
import com.bryan.rubbish_detection_backend.utils.StpKit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

@Aspect
@Component
public class CurrentUserAspect {
    @Before("@annotation(checkCurrentUser)")
    public void checkCurrentUserMethod(JoinPoint joinPoint, @NotNull CheckCurrentUser checkCurrentUser) throws Throwable {
        long currentUserId;
        if (checkCurrentUser.role() == CheckCurrentUser.Role.USER) {
            currentUserId = StpKit.USER.getLoginIdAsLong();
        } else {
            currentUserId = StpKit.ADMIN.getLoginIdAsLong();
        }

        int index = checkCurrentUser.paramIndex();
        Object[] args = joinPoint.getArgs();
        if (index < 0 || index >= args.length) {
            throw new CustomException("未指定正确的参数索引用于校验用户权限");
        }

        Object arg = args[index];
        checkUser(arg, checkCurrentUser.value(), currentUserId);
    }

    /**
     * 校验传入的参数中对应的用户ID是否与当前登录用户ID一致
     *
     * @param arg           方法参数，可能为 Integer 或包含用户ID字段的对象
     * @param fieldName     当 arg 为对象时，用于获取用户ID的字段名称
     * @param currentUserId 当前登录用户ID
     * @throws Throwable 校验失败时抛出异常
     */
    private void checkUser(Object arg, String fieldName, Long currentUserId) throws Throwable {
        if (arg == null) {
            throw new CustomException("参数异常");
        }

        // 如果参数直接为 Integer 类型
        if (arg instanceof Long) {
            if (!Objects.equals(arg, currentUserId)) {
                throw new CustomException("您无权限操作");
            }
        } else {
            // 否则，利用反射获取指定字段值
            Field field;
            try {
                field = arg.getClass().getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                throw new CustomException("参数异常");
            }
            field.setAccessible(true);
            Object fieldValue = field.get(arg);
            if (fieldValue == null || !Objects.equals(fieldValue, currentUserId)) {
                throw new CustomException("您无权限操作");
            }
        }
    }
}
