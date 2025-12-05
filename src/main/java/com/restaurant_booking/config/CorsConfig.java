package com.restaurant_booking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") //allow all urls
                        .allowedOrigins("https://restaurant-booking-system-main-frontend.onrender.com") //allow react port
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS"); // Allow these actions
            }
        };
    }
}
