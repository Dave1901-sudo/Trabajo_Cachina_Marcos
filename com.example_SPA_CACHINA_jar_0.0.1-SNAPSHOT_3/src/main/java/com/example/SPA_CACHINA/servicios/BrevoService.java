/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.servicios;

/**
 *
 * @author David
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BrevoService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${brevo.api.key}")
    private String apiKey;

    public void enviarCorreo(String destinatario,
                             String asunto,
                             String contenidoHtml) {

        String url = "https://api.brevo.com/v3/smtp/email";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("api-key", apiKey);

        Map<String, Object> body = new HashMap<>();

        Map<String, String> sender = new HashMap<>();
        sender.put("name", "Cachina Fish");
        sender.put("email", "silvadave678@gmail.com");

        body.put("sender", sender);

        body.put("to", List.of(
                Map.of("email", destinatario)
        ));

        body.put("subject", asunto);
        body.put("htmlContent", contenidoHtml);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        try {

            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, request, String.class);

            System.out.println("Correo enviado correctamente.");
            System.out.println(response.getBody());

        } catch (Exception e) {

            System.out.println("Error al enviar correo:");
            e.printStackTrace();

            throw e;
        }
    }
}