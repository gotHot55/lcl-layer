package com.micro.lcl.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.lcl.system.model.SysPermission;
import com.micro.lcl.system.repository.SysPermissionRepository;
import com.micro.lcl.system.service.SysPermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/2316:15
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionRepository, SysPermission> implements SysPermissionService {
    @Override
    public List<SysPermission> queryByUser(String username) {
        return baseMapper.queryByUser(username);
    }
}
