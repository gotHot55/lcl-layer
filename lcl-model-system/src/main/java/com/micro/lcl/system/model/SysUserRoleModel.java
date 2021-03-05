package com.micro.lcl.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/2418:01
 */
@Data
@TableName("sys_user_role")
public class SysUserRoleModel {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer user_id;

    private Integer role_id;

    public SysUserRoleModel() {
    }

    public SysUserRoleModel(Integer user_id, Integer role_id) {
        this.user_id = user_id;
        this.role_id = role_id;
    }
}
