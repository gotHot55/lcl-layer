package com.micro.lcl.system.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.micro.lcl.system.model.SysUserRoleModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色表
 *
 * @author Administrator
 * @date 2021/2/2015:25
 */
@Mapper
@Repository
public interface SysUserRoleRepository extends BaseMapper<SysUserRoleModel> {

    @Select("SELECT role_code FROM sys_role WHERE id IN (SELECT role_id FROM sys_user_role WHERE user_id IN (SELECT id FROM sys_user WHERE username=#{username}))")
    List<String> getRoleByUserName(@Param("username") String username);
}
