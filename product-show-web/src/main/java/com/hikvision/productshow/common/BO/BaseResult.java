package com.hikvision.productshow.common.BO;

import com.hikvision.productshow.common.exception.ApiException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Optional;

/**
 * @description:
 * @creator vv
 * @date 2021/5/17 19:27
 */

@ApiModel(description = "提供的统一返回格式")
public class BaseResult<T> implements Serializable {
    private static final long serialVersionUID = 6803323956728517039L;

    private static final String SUCCESS_CODE = "0";

    @ApiModelProperty(required = true, value = "错误码", dataType = "String", example = "0", position = 1)
    private String code;

    @ApiModelProperty(required = true, value = "返回描述信息", dataType = "String", example = "SUCCESS", position = 2)
    private String msg;

    @ApiModelProperty(value = "返回数据封装", position = 3)
    private T data;

    public BaseResult() {
    }

    public BaseResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResult(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> BaseResult<T> success() {
        return new BaseResult("0", "SUCCESS", null);
    }

    public static <T> BaseResult<T> success(T t) {
        return new BaseResult("0", "SUCCESS", t);
    }

    public static <T> BaseResult<T> fail(String code, String message) {
        return new BaseResult(code, message);
    }

    public static <T> BaseResult<T> fail(String code, String message, T data) {
        return new BaseResult(code, message, data);
    }

    public static <T> BaseResult<T> error(String code, String message) {
        return new BaseResult(code, message);
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Optional<T> safeGetData() {
        if (!"0".equals(this.code)) {
            throw new ApiException(this.code, this.msg);
        } else {
            return Optional.ofNullable(this.data);
        }
    }

    @Transient
    public boolean isSuccess() {
        return "0".equals(this.code);
    }

    public String toString() {
        return "BaseResult(code=" + this.getCode() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ")";
    }
}
