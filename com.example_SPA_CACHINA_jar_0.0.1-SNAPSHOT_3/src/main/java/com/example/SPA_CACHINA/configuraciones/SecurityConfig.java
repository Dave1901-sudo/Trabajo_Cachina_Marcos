/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.configuraciones;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 *
 * @author David
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http

            // 🔴 CSRF (solo para endpoints que realmente lo necesitan)
            .csrf(csrf -> csrf.ignoringRequestMatchers(
                    "/logout",
                    "/realizarPedido",
                    "/updateContactos",
                    "/mis-pedidos/**",
                    "/cancelarMiReserva",
                    "/api/**"
            ))

            // 🔵 AUTORIZACIÓN
            .authorizeHttpRequests(auth -> auth

                // públicos del sistema
                .requestMatchers(
                        "/login",
                        "/register",
                        "/error"
                ).permitAll()

                // actuator (CRÍTICO para Render / Datadog)
                .requestMatchers("/actuator/**").permitAll()

                // recursos estáticos
                .requestMatchers(
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/img/**",
                        "/assets/**",
                        "/uploads/**",
                        "/favicon.ico",
                        "/webjars/**",
                        "/public/**"
                ).permitAll()

                // APIs públicas
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/api/users/register").permitAll()

                // protegidos
                .requestMatchers("/").authenticated()
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/pedidos/**").hasAuthority("ADMIN")

                .anyRequest().authenticated()
            )

            // 🔵 LOGIN
            .formLogin(form -> form
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/", true) // 🔥 evita /error?continue
                .permitAll()
            )

            // 🔵 LOGOUT
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
            )

            // 🔵 SESIONES (ESTABLE Y SIN BASURA)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionUrl("/login")
                .maximumSessions(1)
                .expiredUrl("/login")
            )

            .build();
    }

    // 🔵 Redirección segura tras login
    @Bean
    public AuthenticationSuccessHandler successHandlerOK() {
        return (request, response, authentication) -> {
            response.sendRedirect("/"); // Redirigir después de un login exitoso
        };
    }

    @Bean
    public HttpSessionListener httpSessionListener() {
        return new HttpSessionListener() {
            @Override
            public void sessionCreated(HttpSessionEvent se) {
                System.out.println("Session created: " + se.getSession().getId());
                se.getSession().setMaxInactiveInterval(6000); // Configurar el tiempo de inactividad de la sesión (60 segundos)
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent se) {
                System.out.println("Session destroyed: " + se.getSession().getId());
            }
        };
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher(); // Para registrar eventos de sesión
    }

    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http.authorizeHttpRequests()
                .requestMatchers("/tester/**").permitAll()
                .anyRequest().authenticated()
            .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/admin/resumenVentas",true)
                    .permitAll()
            .and()
                .logout()
                    .logoutUrl("/logout")
                    .permitAll()
            .and()
            .build();
    }*/
}
