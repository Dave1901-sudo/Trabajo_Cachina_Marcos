/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.controladores;

import com.example.SPA_CACHINA.entidades.Usuario;
import com.example.SPA_CACHINA.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 *
 * @author David
 */
@ControllerAdvice
public class GlobalControllerAdvice { // Atributos globales del modelo para todas las vistas

    @Autowired
    private UsuarioService usuarioService;

    @ModelAttribute("isAdmin")
    public boolean isAdmin(Authentication authentication) { // Expone estado admin a todas las vistas
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
        }
        return false;
    }
    

    @ModelAttribute("currentUser")
    public String getCurrentUser(Authentication authentication) { // Expone nombre de usuario a todas las vistas
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    @ModelAttribute("currentUserData")
    public Usuario getCurrentUserData(Authentication authentication) { // Expone datos completos del usuario a todas las vistas
        if (authentication != null && authentication.isAuthenticated()) {
            return usuarioService.getByUsername(authentication.getName());
        }
        return null;
    }

}
