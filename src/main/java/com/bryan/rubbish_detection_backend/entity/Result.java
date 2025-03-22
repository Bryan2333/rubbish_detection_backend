package com.bryan.rubbish_detection_backend.entity;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    private String code;
    private String message;
    private T data;

    public static <T> @NotNull Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode("1000");
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static <T> @NotNull Result<T> success() {
        return success(null);
    }

    public static <T> @NotNull Result<T> error(String code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
