package com.micro.lcl.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.lcl.common.api.model.Result;
import com.micro.lcl.common.api.model.model.LoginUserModel;

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
}
