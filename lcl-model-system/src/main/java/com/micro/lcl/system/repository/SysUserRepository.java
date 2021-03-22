package com.micro.lcl.system.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.micro.lcl.common.api.model.LoginUserModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/1011:53
 */
@Repository
@Mapper
public interface SysUserRepository extends BaseMapper<LoginUserModel> {
}
