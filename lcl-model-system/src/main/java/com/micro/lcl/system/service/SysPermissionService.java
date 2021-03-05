package com.micro.lcl.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.lcl.system.model.SysPermission;

import java.util.List;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/2316:13
 */
public interface SysPermissionService extends IService<SysPermission> {
    List<SysPermission> queryByUser(String username);
}
