package com.project.webpos.Security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  // Disables HTML login rendered by Spring
  // This method is good when you are building REST APIs or Stateless backends(JWTs)
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .formLogin(form -> form.disable())
      .httpBasic(basic -> basic.disable())
      .cors(cors -> cors.configurationSource(corsConfigurationSource()));
    
    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource(){
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("http://localhost:5173")); // Allow your frontend origin
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH" ,"OPTIONS")); // List of allowed methods
    config.setAllowedHeaders(List.of("*")); // Allowed headers
    config.setAllowCredentials(true); // Required for Authorization header or cookies

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config); // Apply to all endpoints.
    return source;
  }
}
