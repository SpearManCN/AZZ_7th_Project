package com.azz.azz;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/","/signIn","/assets/**").permitAll() // 누구나 접근 가능
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )
                .formLogin(form -> form // 로그인 설정
                        .loginPage("/#signIn") // 커스텀 로그인 페이지 경로
                        .loginProcessingUrl("/signIn")
                        .defaultSuccessUrl("/home")
                        .failureUrl("/")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }
}
