package com.middle.test.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/3/210:56
 */
@RestController
@Slf4j
public class HelloController {
    @Value("${server.port}")
    public Integer port;
    @Value("${spring.application.name}")
    public String applicationName;
    @Value("${spring.rabbitmq.username}")
    public String rabbitUsername;
    @Value("${spring.rabbitmq.password}")
    public String rabbitPassword;
    @Value("${spring.datasource.url}")
    public String dbUrl;
    @Value("${spring.datasource.username}")
    public String dbUsername;
    @Value("${spring.datasource.password}")
    public String dbPassword;
    @Value("${spring.datasource.type}")
    public String dbType;
    @GetMapping("/hello")
    public String hello() {
        log.error("port:{},applicationName:{},rabbitUsername:{},rabbitPassword:{},dbUrl:{},dbUsername:{},dbPassword:{},dbType:{}",
                port, applicationName, rabbitUsername, rabbitPassword, dbUrl, dbUsername, dbPassword, dbType);
        return port+","+applicationName+","+rabbitUsername+","+rabbitPassword+","+dbUrl+","+dbUsername+","+dbPassword+","+dbType;
    }
}
