
package com.schoolapp.config;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

        @Autowired
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        @Bean

        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .csrf(csrf -> csrf.disable())

                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                .authorizeHttpRequests(auth -> auth

                                                // Preflight
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                                                // Public endpoints
                                                .requestMatchers(
                                                                "/error",
                                                                "/api/auth/**",
                                                                "/api/public/**",
                                                                "/api/production/**",
                                                                "/api/material-master/**",
                                                                "/api/workflow/**",
                                                                "/api/reports/**",
                                                                "/api/casting-report/**",
                                                                "/api/wire-cutting/**",
                                                                "/api/autoclave/**",
                                                                "/api/block-separating/**",
                                                                "/api/cube-test/**",
                                                                "/api/rejection/**",
                                                                "/api/km-batch/**",
                                                                "/api/km-entry/**",
                                                                "/api/receipts/**",
                                                                "/api/users/**",
                                                                "/api/party-prices/**",
                                                                "/api/leads/**",
                                                                "/api/projects/**",
                                                                "/api/inquiries/**",
                                                                "/api/inquiry-schedule/**",
                                                                "/api/location/**",
                                                                "/api/employees/**",
                                                                "/api/roots/**",
                                                                "/api/user-role-details/**",
                                                                "/api/customer-trn/**",
                                                                "/swagger-ui/**",
                                                                "/v3/api-docs/**")
                                                .permitAll()

                                                // Example public GET
                                                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                                                .requestMatchers("/api/batch/**").permitAll()
                                                .requestMatchers("/api/km-batch/**").permitAll()
                                                .requestMatchers("/api/km-entry/**").permitAll()

                                                // User
                                                .requestMatchers("/api/cart/**", "/api/orders/**")
                                                .hasRole("USER")

                                                .requestMatchers("/api/admin/**")
                                                .permitAll()

                                                .anyRequest().authenticated())

                                .formLogin(form -> form.disable())
                                .httpBasic(basic -> basic.disable())

                                .exceptionHandling(ex -> ex.authenticationEntryPoint(
                                                (request, response, exception) -> {
                                                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                                        response.setContentType("application/json");
                                                        response.getWriter().write("""
                                                                        {
                                                                          "error": "UNAUTHORIZED",
                                                                          "message": "Invalid or missing JWT token"
                                                                        }
                                                                        """);
                                                }));

                http.addFilterBefore(jwtAuthenticationFilter,
                                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {

                CorsConfiguration config = new CorsConfiguration();

                config.setAllowedOriginPatterns(List.of(
                                "http://localhost:*",
                                "http://127.0.0.1:*",
                                "http://103.168.19.63:*"));

                config.setAllowedMethods(List.of(
                                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

                config.setAllowedHeaders(List.of("*"));
                config.setAllowCredentials(true);
                config.setMaxAge(3600L);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

                source.registerCorsConfiguration("/**", config);
                return source;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder(12);
        }

        @Bean
        public AuthenticationManager authenticationManager(
                        AuthenticationConfiguration configuration) throws Exception {
                return configuration.getAuthenticationManager();
        }
}