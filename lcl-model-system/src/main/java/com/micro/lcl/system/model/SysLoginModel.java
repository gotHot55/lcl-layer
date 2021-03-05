package com.micro.lcl.system.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/1011:31
 */
@Data
@ApiModel(value = "登录对象",description = "登录对象")
public class SysLoginModel {
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("验证码")
    private String captcha;
    @ApiModelProperty("验证码key")
    private String checkKey;
}
