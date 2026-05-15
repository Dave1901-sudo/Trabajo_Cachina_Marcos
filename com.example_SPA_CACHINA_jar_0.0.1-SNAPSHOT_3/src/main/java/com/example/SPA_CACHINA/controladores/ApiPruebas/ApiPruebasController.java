/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.controladores.ApiPruebas;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author David
 */
@RestController
public class ApiPruebasController {

    @GetMapping("/api/postman-test")
    public String pruebaPostman() {
        return "API funcionando correctamente para pruebas con Postman";
    }
}