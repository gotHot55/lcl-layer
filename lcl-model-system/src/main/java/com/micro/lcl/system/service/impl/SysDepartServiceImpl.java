package com.micro.lcl.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.lcl.system.model.SysDepartModel;
import com.micro.lcl.system.repository.SysDepartRepository;
import com.micro.lcl.system.service.SysDepartService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/1817:54
 */
@Service
public class SysDepartServiceImpl extends ServiceImpl<SysDepartRepository, SysDepartModel> implements SysDepartService {
    @Override
    public List<SysDepartModel> queryUserDeparts(String userId) {
//        return baseMapper.queryUserDeparts(userId);
        return null;
    }
}
