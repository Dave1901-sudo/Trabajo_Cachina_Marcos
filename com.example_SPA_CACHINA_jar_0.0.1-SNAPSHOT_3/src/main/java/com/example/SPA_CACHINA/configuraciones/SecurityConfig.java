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
// Org. Ing. Jorge Chicana
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.ignoringRequestMatchers("/logout", "/realizarPedido", 
                "/updateContactos","/login", 
                "/api/registrar/**","/api/reservas/**", 
                "/api/login/**","/api/contactos/**",
                "/api/platos/**","/api/reclamaciones/**",
                "/api/sugerencias/**","/api/pedidos/**"))
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/register").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/api/users/register").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/").authenticated()
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/pedidos/**").hasAuthority("ADMIN")
                .requestMatchers(
                        "/formResultadoPlatos",
                        "/platos",
                        "/registrarPlatos",
                        "/getEditPlatos/**",
                        "/deletePlatos"
                ).hasAuthority("ADMIN")
                .requestMatchers(
                        "/formResultadoReclamaciones",
                        "/getEditReclamaciones/**",
                        "/deleteReclamaciones"
                ).hasAuthority("ADMIN")
                .requestMatchers(
                        /*"/formResultadoReservas",*/
                        "/getEditReservas/**",
                        "/deleteReservas"
                ).hasAuthority("ADMIN")
                .requestMatchers(
                        "/formResultadoContactos",
                        "/getEdit/**",
                        "/delete",
                        "/updateContactos"
                ).hasAuthority("ADMIN")
                .requestMatchers(
                       /* "/formResultadoSugerencias",*/
                        "/getEditS/**",
                        "/deleteS"
                ).hasAuthority("ADMIN")
                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/", false)
                .permitAll()
                )
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                )
                .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Define cuándo se debe crear una nueva sesión
                .invalidSessionUrl("/login") // Redirigir a login si la sesión es inválida
                .maximumSessions(1) // Permitir solo una sesión por usuario
                .expiredUrl("/login") // Redirigir a login si la sesión ha expirado
                ) // Configuración para migrar sesión si se produce un ataque de fijación de sesión
                .build();
    }

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
