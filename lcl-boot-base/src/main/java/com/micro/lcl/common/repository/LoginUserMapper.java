package com.micro.lcl.common.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.micro.lcl.common.api.model.model.LoginUserModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/815:46
 */
@Repository
@Mapper
public interface LoginUserMapper extends BaseMapper<LoginUserModel> {
}
