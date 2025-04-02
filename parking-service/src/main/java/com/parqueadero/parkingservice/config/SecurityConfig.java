package com.parqueadero.parkingservice.config;

import com.parqueadero.parkingservice.exception.CustomAccessDeniedHandler;
import com.parqueadero.parkingservice.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtTokenFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(authz -> authz

                    .requestMatchers("/api/auth/login", "/api/user/registro").permitAll()
                    .requestMatchers("/api/user").hasRole("ADMIN")
                    .requestMatchers("/api/user/create-socio").hasRole("ADMIN")
                    .requestMatchers("/api/parkings").hasRole("ADMIN")
                    .requestMatchers("/api/parkings/mine").hasAnyRole("ADMIN", "SOCIO")
                    .requestMatchers("/api/parkings/*").hasRole("ADMIN")
                    .requestMatchers("/api/vehicles/entry").hasRole("SOCIO")
                    .requestMatchers("/api/vehicles/exit").hasRole("SOCIO")
                    .requestMatchers("/api/parkings/*/my-vehicles").hasRole("SOCIO")
                    .requestMatchers("/api/parkings/*/my-vehicles/*").hasRole("SOCIO")
                    .requestMatchers("/api/indicators/top-vehicles-all").hasAnyRole("ADMIN", "SOCIO")
                    .requestMatchers("/api/indicators/top-vehicles/*").hasAnyRole("ADMIN", "SOCIO")
                    .requestMatchers("/api/indicators/first-time-vehicles/*").hasAnyRole("ADMIN", "SOCIO")
                    .requestMatchers("/api/indicators/top-owners").hasRole("ADMIN")
                    .requestMatchers("/api/indicators/top-parkings").hasRole("ADMIN")
                    .requestMatchers("/api/indicators/revenues/*").hasRole("SOCIO")
                    .requestMatchers("/api/indicators/revenue/*").hasRole("SOCIO")
                    .requestMatchers("/api/email/send").hasRole("ADMIN")
                    .anyRequest().authenticated()
            )
            .exceptionHandling(exception ->
                exception.accessDeniedHandler(accessDeniedHandler)
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}