package com.micro.lcl.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.lcl.system.model.SysUserRoleModel;
import com.micro.lcl.system.repository.SysUserRoleRepository;
import com.micro.lcl.system.service.SysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/2015:33
 */
@Service
@Slf4j
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleRepository, SysUserRoleModel> implements SysUserRoleService {

    @Override
    public Set<String> getRoleByUsername(String username) {
        List<String> roles = baseMapper.getRoleByUserName(username);
        log.debug("用户:{}，拥有权限：{}", username, roles);
        return new HashSet<>(roles);
    }

}
