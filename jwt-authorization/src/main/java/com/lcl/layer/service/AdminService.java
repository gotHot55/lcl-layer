package com.lcl.layer.service;

import com.lcl.layer.model.UserModel;

/**
 * Todo
 *
 * @author Administrator
 * @date 2020/12/2117:59
 */
public interface AdminService {
    UserModel loadUserByUsername(String username);
}
