package com.personal.bulletinboardsystem.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig{
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //2022-12-31 http.crsf().disable()을 하지 않으면 아래 permitAll 설정을 해주어도 403에러가 나타난다.
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/**").permitAll();
        return http.build();
    }
    // @Bean
    // public WebSecurityCustomizer webSecurityCustomizer(WebSecurity web) {
    //     return (web) -> web.ignoring().antMatchers("/**");
    // }
}
