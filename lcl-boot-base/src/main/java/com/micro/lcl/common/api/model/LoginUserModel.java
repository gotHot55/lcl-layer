package com.micro.lcl.common.api.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/417:21
 */
@Data
@Accessors(chain = true)
@TableName(value = "sys_user")
public class LoginUserModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     *  登录人账号
     */
    private String username;
    /**
     * 登录人姓名
     */
    private String realname;
    /**
     *  登录人密码
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    /**
     * 盐
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String salt;
    /**
     * 当前登录部门code
     */
    private String orgCode;
    /**
     * 头像
     */
    private String avatar;
    /**
     *生日
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    /**
     * 性别 1：男  2：女
     */
    private Integer sex;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 电话
     */
    private String phone;
    /**
     * 状态: 0正常  1冻结
     */
    private Integer status;
    /**
     *删除状态
     */
    @TableLogic
    private Integer delFlag;
    /**
     * 工号，唯一键
     */
    private String workNo;
    /**
     * 职务
     */
    private String post;
    /**
     * 座机号
     */
    private String telephone;

    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 简单介绍
     */
    private String introduction;

    /**
     * 第三方登录的唯一标识
     */
    private String thirdId;
    /**
     * 第三方类型 <br>
     * （github/github，wechat_enterprise/企业微信，dingtalk/钉钉）
     */
    private String thirdType;

    @TableField(exist = false)
    private String roles;
}
