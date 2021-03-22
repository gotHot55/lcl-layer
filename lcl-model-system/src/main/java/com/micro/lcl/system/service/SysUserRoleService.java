package com.micro.lcl.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.lcl.system.model.SysUserRoleModel;

import java.util.Set;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/2015:32
 */
public interface SysUserRoleService extends IService<SysUserRoleModel> {
    Set<String> getRoleByUsername(String username);
}
