package com.micro.lcl.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Administrator
 * @date 2021/2/2216:38
 */
@Configuration
@MapperScan(value = {"com.micro.lcl.**.repository"})
public class MybatisConfig {

}
