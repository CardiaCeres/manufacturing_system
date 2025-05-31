package com.manufacturing.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @SuppressWarnings("null")
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 設置允許跨域的路徑
                .allowedOrigins("https://manufacturing-system-springboot.onrender.com")  // 允許來自 localhost:8082 的請求
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 允許的HTTP方法
                .allowedHeaders("*")  // 設置允許的請求頭
                .allowCredentials(true);  // 設置是否允許帶有憑證的請求
    }
}