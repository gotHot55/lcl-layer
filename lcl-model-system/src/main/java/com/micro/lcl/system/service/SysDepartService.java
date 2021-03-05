package com.micro.lcl.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.lcl.system.model.SysDepartModel;

import java.util.List;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/1817:52
 */
public interface SysDepartService extends IService<SysDepartModel> {
    List<SysDepartModel> queryUserDeparts(String userId);
}
