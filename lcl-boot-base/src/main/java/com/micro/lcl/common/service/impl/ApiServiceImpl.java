package com.micro.lcl.common.service.impl;

import com.micro.lcl.common.api.model.LoginUserModel;
import com.micro.lcl.common.service.ApiService;
import com.micro.lcl.common.service.LoginUserService;
import com.micro.lcl.common.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/816:21
 */
@Service
public class ApiServiceImpl implements ApiService {

    private final UserRoleService userRoleService;
    private final LoginUserService loginUserService;

    public ApiServiceImpl(UserRoleService userRoleService, LoginUserService loginUserService) {
        this.userRoleService = userRoleService;
        this.loginUserService = loginUserService;
    }

    @Override
    public Set<String> getRoleByUserName(String username) {
        return userRoleService.getRoleByUserName(username);
    }

    @Override
    public LoginUserModel getUserByName(String username) {
        return loginUserService.getUserByName(username);
    }

}
