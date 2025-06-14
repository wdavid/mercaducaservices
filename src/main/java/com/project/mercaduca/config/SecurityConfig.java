package com.project.mercaduca.config;

import com.project.mercaduca.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/business-requests").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/business-requests").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/business-requests/gender").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/business-requests/entrepeneurkind").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/business-requests/*/approve").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/business-requests/*/reject").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/business-requests").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/logout").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/category").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/category").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/products").hasRole("EMPRENDEDOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/products/*/review").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/products/pending").hasRole("ADMIN")
                        //.requestMatchers(HttpMethod.GET, "/api/products/*").hasRole("EMPRENDEDOR")
                        .requestMatchers(HttpMethod.GET, "/api/products/").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/business/*/approved").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/business-requests/approved-summary").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/major").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/major/*/majors").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/contract/create").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/contract/payment").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/admin/products/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/admin/business/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/*").hasRole("EMPRENDEDOR")
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:3000", "https://mercaduca-front.vercel.app"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
