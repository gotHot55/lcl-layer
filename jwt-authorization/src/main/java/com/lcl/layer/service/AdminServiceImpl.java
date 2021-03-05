package com.lcl.layer.service;

import com.lcl.layer.model.UserModel;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

/**
 * Todo
 *
 * @author Administrator
 * @date 2020/12/2118:06
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Override
    public UserModel loadUserByUsername(String username) {
        UserModel user = new UserModel();
        user.setUsername(username);
        user.setPassword("123456");
        return user;
    }
}
