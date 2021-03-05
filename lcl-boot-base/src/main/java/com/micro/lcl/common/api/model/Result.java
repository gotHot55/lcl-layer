package com.micro.lcl.common.api.model;

import com.micro.lcl.constant.BaseConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/217:14
 */
@Data
@ApiModel(value = "接口返回对象", description = "接口返回对象")
public class Result<T> implements Serializable {
    public static final Long serialVersionUID = 1L;
    /**
     * 成功标志
     */
    @ApiModelProperty(value = "成功标志")
    private boolean success = true;
    /**
     * 返回处理消息
     */
    @ApiModelProperty(value = "返回处理消息")
    private String message = "操作成功";
    /**
     * 返回代码
     */
    @ApiModelProperty(value = "返回代码")
    private Integer code = 0;
    /**
     * 返回数据对象
     */
    @ApiModelProperty(value = "返回数据对象")
    private T result;
    /**
     * 时间戳
     */
    @ApiModelProperty(value = "时间戳")
    private long timestamp = System.currentTimeMillis();

    public Result() {
    }

    public Result<T> success(String message) {
        this.message = message;
        this.code = BaseConstant.SC_OK_200;
        this.success = true;
        return this;
    }

    public static <T> Result<T> OK() {
        Result<T> r = new Result<>();
        r.setCode(BaseConstant.SC_OK_200);
        r.setMessage("成功");
        r.setSuccess(true);
        return r;
    }

    public static <T> Result<T> OK(T data) {
        Result<T> r = new Result<>();
        r.setSuccess(true);
        r.setCode(BaseConstant.SC_OK_200);
        r.setResult(data);
        return r;
    }

    public static <T> Result<T> OK(String message, T data) {
        Result<T> r = new Result<>();
        r.setMessage(message);
        r.setSuccess(true);
        r.setResult(data);
        return r;
    }

    public static <T> Result<T> error(int code, String message) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMessage(message);
        r.setSuccess(false);
        return r;
    }

    public static <T> Result<T> error(String message) {
        return error(BaseConstant.SC_ERROR_500, message);
    }

    public Result<T> error500(String message) {
        this.message = message;
        this.code = BaseConstant.SC_ERROR_500;
        this.success = false;
        return this;
    }

    /**
     * 无访问权限结果
     */
    public static Result<Object> noauth(String message) {
        return error(BaseConstant.SC_NO_AUTHZ, message);
    }

}
