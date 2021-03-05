package com.micro.lcl.system.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.micro.lcl.system.model.SysDepartModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/1817:40
 */
@Mapper
@Repository
public interface SysDepartRepository extends BaseMapper<SysDepartModel> {
//    List<SysDepartModel> queryUserDeparts(@Param("userId") String userId);
}
