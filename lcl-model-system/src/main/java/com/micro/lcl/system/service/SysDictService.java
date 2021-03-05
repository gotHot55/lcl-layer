package com.micro.lcl.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.lcl.system.model.DictModel;
import com.micro.lcl.system.model.SysDictModel;

import java.util.List;
import java.util.Map;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/2216:52
 */
public interface SysDictService extends IService<SysDictModel> {
    Map<String, List<DictModel>> queryAllDictItems();
}
