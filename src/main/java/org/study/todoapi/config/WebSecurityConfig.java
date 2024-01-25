package org.study.todoapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;
import org.study.todoapi.filter.JwtAuthFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    // 시큐리티 기본 보안설정 해제
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .cors()
                .and()
                .csrf().disable()
                .httpBasic().disable()
                // 세션 인증은 더이상 사용하지 않겠다
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 어떤 요청에서는 인증을하고 어떤 요청에서는 인증을 안할 건지 설정
                .authorizeRequests() // 어떤 요청에서 인증을 할꺼냐
                    .antMatchers("/", "/api/auth/**").permitAll() // 이 요청은 인증을 안해두 됨
                    //.antMatchers(HttpMethod.POST, "/api/todos").permitAll() // api/todos POST는 인증 ㄴㄴ
                    //.antMatchers("/**").hasRole("ADMIN") //
                .anyRequest().authenticated() // 나머지 요청은 인증(로그인) 받아라
        ;



        // 토큰인증 필터 연결하기
        http.addFilterAfter(jwtAuthFilter, CorsFilter.class);
        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
