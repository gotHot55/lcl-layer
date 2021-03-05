package com.lcl.layer.service;

import com.lcl.layer.model.UserModel;
import org.springframework.core.type.MethodMetadata;
import org.springframework.stereotype.Service;

/**
 * Todo
 *
 * @author Administrator
 * @date 2020/12/2118:09
 */
@Service
public class MemberServiceImpl implements MemberService {
    @Override
    public UserModel loadUserByUsername(String username) {
        UserModel user = new UserModel();
        user.setUsername(username);
        user.setPassword("987654");
        return user;
    }
}
