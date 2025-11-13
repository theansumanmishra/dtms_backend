package com.disputetrackingsystem.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origins:}")
    private String allowedOrigins; // comma-separated

    @Value("${app.cors.allowed-origin-patterns:}")
    private String allowedOriginPatterns; // comma-separated (supports wildcards)

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);

        // exact origins
        if (!allowedOrigins.isBlank()) {
            List<String> origins = Arrays.stream(allowedOrigins.split(","))
                    .map(String::trim).filter(s -> !s.isBlank())
                    .collect(Collectors.toList());
            configuration.setAllowedOrigins(origins);
        }

        // wildcard patterns e.g. https://*.vercel.app
        if (!allowedOriginPatterns.isBlank()) {
            List<String> patterns = Arrays.stream(allowedOriginPatterns.split(","))
                    .map(String::trim).filter(s -> !s.isBlank())
                    .collect(Collectors.toList());
            configuration.setAllowedOriginPatterns(patterns);
        }

        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization", "Location"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
