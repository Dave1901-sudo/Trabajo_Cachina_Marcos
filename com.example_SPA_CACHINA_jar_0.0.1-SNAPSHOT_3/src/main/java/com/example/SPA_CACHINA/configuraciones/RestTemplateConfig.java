/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.configuraciones;

/**
 *
 * @author David
 */
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig { // Bean RestTemplate para llamadas HTTP

    @Bean
    public RestTemplate restTemplate() { // Bean RestTemplate para APIs externas
        return new RestTemplate();
    }
}
