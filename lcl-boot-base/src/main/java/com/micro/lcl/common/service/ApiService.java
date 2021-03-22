package com.micro.lcl.common.service;

import com.micro.lcl.common.api.model.LoginUserModel;

import java.util.Set;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/816:20
 */
public interface ApiService {
    Set<String> getRoleByUserName(String username);

    LoginUserModel getUserByName(String username);
}
