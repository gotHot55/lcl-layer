package com.micro.lcl.system.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.micro.lcl.system.model.DuplicateCheckVo;
import com.micro.lcl.system.model.SysDictModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/2215:59
 */
@Repository
@Mapper
public interface SysDictRepository extends BaseMapper<SysDictModel> {
    int dupliateCheckCountSql(DuplicateCheckVo checkVo);

}
