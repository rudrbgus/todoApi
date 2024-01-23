package org.study.todoapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


// 전역 크로스오리진 설정 : 클라이언트 허용 아이피 설정


@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/api/**") // 어떤 요청 URL을 허용할 지
                .allowedOrigins("http://localhost:3000") // 어떤 클라이언트를 허용할 지
                .allowedMethods("*") // 어떤 요청 방식을 허용할 지
                .allowedHeaders("*") // 어떤 헤더를 허용할 지
                .allowCredentials(true) // 쿠키 전달을 허용할 지 (토큰 도)
                .maxAge(3600) // 허용시간에 대한 캐싱 설정 1시간 마다
                ;
    }
}
