package com.demo.base.domain.response;

import com.demo.base.aspect.mask.MaskManagerDecorator;
import com.demo.base.annotation.mask.Sensitive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.io.Serial;
import java.io.Serializable;

@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Jacksonized
public class Result<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 5851715478786703393L;
    String message;
    @Sensitive(MaskManagerDecorator.PENETRATE)
    T data;
    int code;

    public static <T> Result<T> success(T data) {
        return new Result<T>().setData(data).setCode(200);
    }

    public static <T> Result<T> success(String message) {
        return new Result<T>().setMessage(message).setCode(200);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<T>().setMessage(message).setCode(400);
    }

}