package com.micro.lcl.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.lcl.common.api.Result;
import com.micro.lcl.common.api.model.LoginUserModel;

import java.util.Set;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/1011:47
 */
public interface SysUserService extends IService<LoginUserModel> {
    Result checkUserIsEffective(LoginUserModel userModel);

    void addUserWithRole(LoginUserModel userModel);

    void editUserWithRole(LoginUserModel userModel);

    boolean deleteUser(String id);

    /**
     *通过用户名获取权限集合
     * @param username 用户名
     * @return
     */
    Set<String> getUserPermissionSet(String username);
}
