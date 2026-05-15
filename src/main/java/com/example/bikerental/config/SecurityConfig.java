package com.example.bikerental.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
public class SecurityConfig {

    // @Bean tells Spring to manage this object and use it for global security settings.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF (Cross-Site Request Forgery) protection for simplicity in this project.
                // In a real-world app, you would leave this enabled for security.
                .csrf(AbstractHttpConfigurer::disable)

                // Configure which URL routes require authentication and which do not.
                .authorizeHttpRequests(auth -> auth
                        // Public routes: Anyone can access the login, register, and static CSS/JS files.
                        .requestMatchers("/", "/login", "/register", "/api/session", "/css/**", "/js/**", "/images/**", "/vendor/**", "/fonts/**").permitAll()

                        // Protected Admin routes: Only users holding the "ROLE_ADMIN" authority can access these.
                        .requestMatchers("/admin/**", "/admin").hasRole("ADMIN")

                        // All other routes (e.g., /bikes, /api/rent) require the user to be logged in.
                        .anyRequest().authenticated()
                )

                // Configure the standard Spring Security login form behavior
                .formLogin(form -> form
                        .loginPage("/login") // Custom HTML login page instead of Spring's default
                        .loginProcessingUrl("/spring-security-login")
                        .permitAll()
                )

                // Configure logout behavior
                .logout(logout -> logout
                        .logoutUrl("/spring-security-logout")
                );

        // Build and return the configured security chain (Builder Design Pattern)
        return http.build();
    }
}

