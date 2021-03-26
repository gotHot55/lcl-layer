package com.micro.lcl.common.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.micro.lcl.common.api.model.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/716:54
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
    @Select("select role_code from sys_role where id in (select role_id from sys_user_role where user_id=(select id from sys_user where username=#{username}))")
    List<String> getRoleByUserName(@Param("username") String username);

    @Select("select role_id from sys_user_role where user_id=(select id from sys_user where username=#{username})")
    List<String> getRoleIdByUserName(@Param("username") String username);
}
