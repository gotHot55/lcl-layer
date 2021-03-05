package com.micro.lcl.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.lcl.common.api.model.model.LoginUserModel;
import com.micro.lcl.common.api.model.model.UserRole;
import com.micro.lcl.common.repository.UserRoleMapper;
import com.micro.lcl.common.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/717:18
 */
@Service
@Slf4j
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
    @Resource
    private UserRoleMapper userRoleMapper;


    @Override
    public Set<String> getRoleByUserName(String username) {
        List<String> roles = userRoleMapper.getRoleByUserName(username);
        log.info("-------通过数据库读取用户拥有的角色Rules------username： " + username + ",Roles size: " + (roles == null ? 0 : roles.size()));
        return new HashSet<>(roles);
    }

    @Override
    public Set<String> getRoleIdByUserName(String username) {
        return null;
    }


}
