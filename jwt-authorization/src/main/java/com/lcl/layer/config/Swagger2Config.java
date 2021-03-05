package com.lcl.layer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Todo
 *
 * @author Administrator
 * @date 2020/12/2114:16
 */
//@EnableSwagger2
//@Component
public class Swagger2Config {
    @Bean
    public Docket createSwagger() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lcl.layer.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("jwt登录验证授权")
                .contact(new Contact("jwt-authorization", "ttp://localhost:8089", "595105385@qq.com"))
                .description("使用jwt进行权限和登录的验证")
                .version("1.0")
                .build();
    }
}
