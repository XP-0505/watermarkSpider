package com.example.watermarkSpider.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
       //允许所有跨域
        registry.addMapping("/**") // 允许所有路径
                .allowedOrigins("*") // 允许任何来源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的HTTP方法
                .allowedHeaders("*") // 允许任何头信息
                .maxAge(168000); // 预检请求的有效期，单位为秒
    }
}
