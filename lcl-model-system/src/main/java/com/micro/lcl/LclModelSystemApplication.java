package com.micro.lcl;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
public class LclModelSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LclModelSystemApplication.class, args);
    }

}
