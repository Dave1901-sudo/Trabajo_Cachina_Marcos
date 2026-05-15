/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.controladores.ApiPruebas;

import com.example.SPA_CACHINA.servicios.UsuarioService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registrar")
public class ApiUsuarioTestController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@RequestBody Map<String, String> body) {
        try {
            String username = body.get("username");
            String password = body.get("password");
            String role = body.getOrDefault("role", "USER");

            usuarioService.createUser(username, password, role);

            return ResponseEntity.ok(Map.of(
                "estado", "success",
                "mensaje", "Usuario registrado correctamente",
                "username", username,
                "role", role
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "estado", "error",
                "mensaje", e.getMessage()
            ));
        }
    }
    @GetMapping("/listar")
        public ResponseEntity<?> listarUsuarios() {
            return ResponseEntity.ok(usuarioService.getList());
        }
}