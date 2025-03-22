package com.bryan.rubbish_detection_backend.exception;

import cn.dev33.satoken.exception.SaTokenException;
import com.bryan.rubbish_detection_backend.entity.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.bryan.rubbish_detection_backend")
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<String> error(HttpServletRequest request, Exception e) {
        return Result.error("-1", e.getMessage());
    }

    @ExceptionHandler(SaTokenException.class)
    public Result<String> saTokenError(SaTokenException e) {
        System.out.println(e.getCode());
        return switch (e.getCode()) {
            case 11011, 11012 -> Result.error("401", "您还未登录，请先登录");
            default -> Result.error("500", "系统异常");
        };
    }

    @ExceptionHandler(CustomException.class)
    public Result<String> customError(HttpServletRequest request, CustomException e) {
        return Result.error("-1", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Object> exceptionHandler(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        String message = bindingResult.getFieldErrors().stream().findFirst().map(FieldError::getDefaultMessage).orElse("参数错误");

        return Result.error("-1", message);
    }
}
