package com.micro.lcl;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@Slf4j
public class LclModelSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LclModelSystemApplication.class, args);
        log.error("-------------服务启动成功————————————————————————————");
    }

}
