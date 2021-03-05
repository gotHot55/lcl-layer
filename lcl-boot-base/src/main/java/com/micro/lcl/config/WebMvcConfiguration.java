package com.micro.lcl.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Todo
 *
 * @author Administrator
 * @date 2021/2/915:45
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Value("${boot.path.upload}")
    private String upLoadPath;
    @Value("${boot.path.webapp}")
    private String webAppPath;
//    @Value("${spring.resource.static-locations}")
//    private String staticLocations;

    /**
     * 静态资源的配置 - 使得可以从磁盘中读取 Html、图片、视频、音频等
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("file:" + upLoadPath + "//", "file: " + webAppPath + "//");
//                .addResourceLocations(staticLocations.split(","))
    }
    /**
     * 默认路径访问根路径跳转doc.html页面（swagger页面）
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("/doc.html");
    }

    @Bean
    @Conditional(CorsFilterCondition.class)
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        //是否允许请求带有验证信息
        corsConfiguration.setAllowCredentials(true);
        //允许访问的客户端域名
        corsConfiguration.addAllowedOrigin("*");
        //允许服务端访问的客户端请求头
        corsConfiguration.addAllowedHeader("*");
        //允许访问的方法名，get、post等
        corsConfiguration.addAllowedMethod("*");
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}

