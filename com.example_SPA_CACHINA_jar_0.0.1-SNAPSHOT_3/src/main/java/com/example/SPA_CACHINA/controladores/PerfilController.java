/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.controladores;

import com.example.SPA_CACHINA.entidades.Usuario;
import com.example.SPA_CACHINA.servicios.UsuarioService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author David
 */
@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping
    public String verPerfil(Model model, Principal principal) {

        Usuario usuario = usuarioService.getByUsername(principal.getName());

        model.addAttribute("usuario", usuario);

        return "perfil";
    }
    
    @PostMapping("/actualizar")
    @ResponseBody
    public ResponseEntity<String> actualizarCampo(
            @RequestParam Long id,
            @RequestParam String campo,
            @RequestParam String valor,
            Principal principal) {

        try {

            usuarioService.actualizarCampoPerfil(
                    principal.getName(),
                    id,
                    campo,
                    valor
            );

            return ResponseEntity.ok("OK");

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

}