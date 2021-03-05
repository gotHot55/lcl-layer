package com.micro.lcl.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.lcl.system.model.DictModel;
import com.micro.lcl.system.model.SysDictItemModel;
import com.micro.lcl.system.model.SysDictModel;
import com.micro.lcl.system.repository.SysDictItemRepository;
import com.micro.lcl.system.repository.SysDictRepository;
import com.micro.lcl.system.service.SysDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/2216:54
 */
@Service
@Slf4j
public class SysDictServiceImpl extends ServiceImpl<SysDictRepository, SysDictModel> implements SysDictService {
    private final SysDictRepository dictRepository;
    private final SysDictItemRepository dictItemRepository;

    public SysDictServiceImpl(SysDictRepository dictRepository, SysDictItemRepository dictItemRepository) {
        this.dictRepository = dictRepository;
        this.dictItemRepository = dictItemRepository;
    }


    @Override
    public Map<String, List<DictModel>> queryAllDictItems() {
        Map<String, List<DictModel>> res = new HashMap<>();
        List<SysDictModel> sysDictModels = dictRepository.selectList(null);
        LambdaQueryWrapper<SysDictItemModel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictItemModel::getStatus, 1);
        queryWrapper.orderByAsc(SysDictItemModel::getSortOrder);
        List<SysDictItemModel> itemModelList = dictItemRepository.selectList(queryWrapper);
        for (SysDictModel model : sysDictModels) {
            List<DictModel> dictModelList = itemModelList.stream().filter(s -> model.getId().equals(s.getDictId())).map(item -> {
                DictModel dictModel = new DictModel();
                dictModel.setText(item.getItemText());
                dictModel.setValue(item.getItemValue());
                return dictModel;
            }).collect(Collectors.toList());
            res.put(model.getDictCode(), dictModelList);
        }
        log.debug("---------登录加载系统字典——————————————————————————"+res.toString());
        return res;
    }

}
