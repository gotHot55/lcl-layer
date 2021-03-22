package com.micro.lcl.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.lcl.common.api.model.LoginUserModel;
import org.apache.ibatis.annotations.Param;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/815:45
 */
public interface LoginUserService extends IService<LoginUserModel> {
    LoginUserModel getUserByName(@Param("username") String username);

}
