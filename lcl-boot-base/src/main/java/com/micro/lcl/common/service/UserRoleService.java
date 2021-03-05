package com.micro.lcl.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.lcl.common.api.model.model.LoginUserModel;
import com.micro.lcl.common.api.model.model.UserRole;

import java.util.Set;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/717:25
 */
public interface UserRoleService extends IService<UserRole> {

    Set<String> getRoleByUserName(String username);

    Set<String> getRoleIdByUserName(String username);


}
