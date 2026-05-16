package com.example.bikerental.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
public class SecurityConfig {

    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                
                
                .csrf(AbstractHttpConfigurer::disable)
                
                
                .authorizeHttpRequests(auth -> auth
                        
                        .requestMatchers("/login", "/register", "/css/**", "/js/**", "/images/**", "/vendor/**", "/fonts/**").permitAll()
                        
                        
                        .requestMatchers("/admin/**", "/admin").hasRole("ADMIN")
                        
                        
                        .anyRequest().authenticated()
                )
                
                
                .formLogin(form -> form
                        .loginPage("/login") 
                        .loginProcessingUrl("/spring-security-login")
                        .permitAll()
                )
                
                
                .logout(logout -> logout
                        .logoutUrl("/spring-security-logout")
                );
                
        
        return http.build();
    }
}
