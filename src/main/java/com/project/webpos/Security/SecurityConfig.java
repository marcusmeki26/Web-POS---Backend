package com.project.webpos.Security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.project.webpos.JWT.JWTCheckerPerRequest;
import com.project.webpos.Service.Security.UserDetailService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor 
public class SecurityConfig {
  private final UserDetailService userDetailService;
  private final JWTCheckerPerRequest jwtCheckerPerRequest;

  // =================================================================================================================
  // Overriding the default filter chain. Creating a custom configuration for filter chain.
  // Disables HTML login rendered by Spring.
  // This method is good when you are building REST APIs or Stateless backends(JWTs)
  // =================================================================================================================
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // Old implementation
    // http
    //   .csrf(csrf -> csrf.disable())
    //   .formLogin(form -> form.disable())
    //   .httpBasic(basic -> basic.disable())
    //   .cors(cors -> cors.configurationSource(corsConfigurationSource()));
    
    // return http.build();
    return http
      .csrf(AbstractHttpConfigurer::disable) // disables the csrf
      .authorizeHttpRequests(request -> request
              .requestMatchers("/api/login", "/api/refresh", "/v1/user/create/dummy", "/swagger-ui/**", "/v3/api-docs/**") // This removes the need for the user to be authenticated first before logging in. Basically you can perform a POST method without the need of being authenticated.
              .permitAll()
              .anyRequest().authenticated()) // tells the spring that every http request should be authenticated
      .httpBasic(Customizer.withDefaults()) // this lets you test endpoints quickly from tools like postman.
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // this makes every http request stateless, meaning every request should contain a token.
      .addFilterBefore(jwtCheckerPerRequest, UsernamePasswordAuthenticationFilter.class) // before using the UsernamePasswordAuthenticationFilter go to jwtCheckerPerRequest first
      .cors(cors -> cors.configurationSource(corsConfigurationSource())) // include the cors configuration
      .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource(){
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("http://localhost:5173")); // Allow your frontend origin
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH" ,"OPTIONS")); // List of allowed methods
    config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cache-Control")); // Allowed headers
    config.setAllowCredentials(true); // Required for Authorization header or cookies

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config); // Apply to all endpoints.
    return source;
  }

  // ======================================================================================================================================
  // Overriding the default Authentication. Creating a custom way on how to authenticate a user using DaoAuthenticationProvider.
  // ======================================================================================================================================
  @Bean
  public AuthenticationProvider authenticationProvider(){
      DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailService); // Injecting UserDetailsService to retrieve user information like username, password, roles and, etc. Basically this acts like a bridge for the Dao to authenticate the user's data store.
      provider.setPasswordEncoder(new BCryptPasswordEncoder(12)); // This identifies how you should decrypt the hash for the password to be able to verify if the user is existing. Should match how you encrypt the password.

      return provider;
  }

  // =================================================================================================================
  // Behind the scenes AuthenticationManager is calling AuthenticationProvider. This allows you to have login option.
  // =================================================================================================================
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
      return config.getAuthenticationManager(); // gets the object of AuthenticationManager.
  }
}
