package com.micro.lcl.system.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.micro.lcl.system.model.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/2316:11
 */
@Mapper
@Repository
public interface SysPermissionRepository extends BaseMapper<SysPermission> {
    /**
     * 根据用户查询用户权限
     * @param username
     * @return
     */
    List<SysPermission> queryByUser(@Param("username") String username);
}
