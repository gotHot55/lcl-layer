package com.micro.lcl.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.lcl.common.api.model.LoginUserModel;
import com.micro.lcl.common.repository.LoginUserMapper;
import com.micro.lcl.common.service.LoginUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/815:46
 */
@Service
public class LoginUserServiceImpl extends ServiceImpl<LoginUserMapper, LoginUserModel> implements LoginUserService {
    private final LoginUserMapper loginUserMapper;
    @Autowired
    public LoginUserServiceImpl(LoginUserMapper loginUserMapper) {
        this.loginUserMapper = loginUserMapper;
    }

    @Override
    public LoginUserModel getUserByName(String username) {
        QueryWrapper<LoginUserModel> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        wrapper.eq("del_flag", 0);
        loginUserMapper.selectOne(wrapper);
        return null;
    }
}
