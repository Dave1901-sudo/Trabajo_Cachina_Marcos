/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.controladores;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 *
 * @author David
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    /*@ModelAttribute("isAdmin")
    public boolean isAdmin(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
            System.out.println("User is admin: " + isAdmin); // Depuración
            return isAdmin;
        }
        return false;
    }*/
    @ModelAttribute("isAdmin")
    public boolean isAdmin(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
        }
        return false;
    }
    

    @ModelAttribute("currentUser")
    public String getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName(); // Nombre del usuario autenticado
        }
        return null; // Si no está autenticado, no hay nombre
    }

}
