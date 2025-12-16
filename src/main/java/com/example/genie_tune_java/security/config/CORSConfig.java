package com.example.genie_tune_java.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CORSConfig {

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.setAllowedOrigins(List.of(
            "http://localhost:3000", // React
            "http://localhost:8000" // Python
    ));
    config.setAllowedMethods(List.of(
            "GET", "POST", "OPTIONS"
    ));
    config.setAllowedHeaders(List.of(
            "Content-Type",
            "Authorization"
    ));
    config.setExposedHeaders(List.of(
            "Set-Cookie"
    ));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    source.registerCorsConfiguration("/**", config);

    return source;

  }
}
