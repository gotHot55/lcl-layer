package com.micro.lcl.system.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.micro.lcl.system.model.SysDictItemModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/2216:09
 */
@Repository
@Mapper
public interface SysDictItemRepository extends BaseMapper<SysDictItemModel> {
}
