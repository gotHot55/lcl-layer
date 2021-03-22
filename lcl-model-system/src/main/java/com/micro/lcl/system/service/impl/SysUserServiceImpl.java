package com.micro.lcl.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.lcl.common.api.Result;
import com.micro.lcl.common.api.model.LoginUserModel;
import com.micro.lcl.common.api.model.UserRole;
import com.micro.lcl.common.service.UserRoleService;
import com.micro.lcl.constant.CacheConstant;
import com.micro.lcl.system.constant.CommonConstant;
import com.micro.lcl.system.repository.SysUserRepository;
import com.micro.lcl.system.repository.SysUserRoleRepository;
import com.micro.lcl.system.service.SysUserRoleService;
import com.micro.lcl.system.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/1011:47
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserRepository, LoginUserModel> implements SysUserService {
    private final UserRoleService userRoleService;

    public SysUserServiceImpl(SysUserRoleService sysUserRoleService, SysUserRoleRepository sysUserRoleRepository, UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @Override
    public Result<?> checkUserIsEffective(LoginUserModel userModel) {
        Result<?> result = new Result<>();
        if (userModel == null) {
            return result.error500("该用户不存在,请注册");
        }
        if (CommonConstant.DEL_FLAG_1.equals(userModel.getDelFlag())) {
            return result.error500("该用户已注销！");
        }
        if (CommonConstant.USER_STATUS.equals(userModel.getStatus())) {
            return result.error500("该用户状态已被锁定，请联系管理员");
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserWithRole(LoginUserModel userModel) {
        save(userModel);
        saveUserRole(userModel);
    }

    private void saveUserRole(LoginUserModel userModel) {
        if (StringUtils.isNotEmpty(userModel.getRoles())) {
            String[] arr = userModel.getRoles().split(",");
            for (String roleId : arr) {
                UserRole userRole = new UserRole(userModel.getId(), Integer.parseInt(roleId));
//                sysUserRoleRepository.insert(sysUserRoleModel);
                userRoleService.save(userRole);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {CacheConstant.SYS_USER_CACHE},allEntries = true)
    public void editUserWithRole(LoginUserModel userModel) {
        baseMapper.updateById(userModel);
        userRoleService.remove(new QueryWrapper<UserRole>().lambda().eq(UserRole::getUserId, userModel.getId()));
        saveUserRole(userModel);
    }
}
