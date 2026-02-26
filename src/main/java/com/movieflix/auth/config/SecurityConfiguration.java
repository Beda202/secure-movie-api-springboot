package com.movieflix.auth.config;

import com.movieflix.auth.service.AuthFilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.Security;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final AuthFilterService authFilterService ;
    private final AuthenticationProvider authenticationProvider ;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/api/v1/auth/**", "/forget-password/**").permitAll()
                        .anyRequest().authenticated()
        );
        http.csrf(AbstractHttpConfigurer ::disable) ;
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) );
        http.authenticationProvider(authenticationProvider);
        http.addFilterBefore(authFilterService , UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
