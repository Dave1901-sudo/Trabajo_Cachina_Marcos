/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.controladores;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 *
 * @author David
 */
@Controller
public class HomeController {

    /*@GetMapping("/home")
    public String home() {
        return "paginaUsuarios";
    }*/
    
    /* @GetMapping("/resumen")
    public String resumen() {
        return "resumenVentas";
    }*/

    @GetMapping("/admin")
    public String admin() {
        return "paginaAdmin";
    }
    

}
