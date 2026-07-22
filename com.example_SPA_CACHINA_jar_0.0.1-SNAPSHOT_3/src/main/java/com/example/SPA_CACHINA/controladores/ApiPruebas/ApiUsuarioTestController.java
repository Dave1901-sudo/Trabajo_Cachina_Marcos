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
public class ApiUsuarioTestController { // API para pruebas de registro de usuario

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@RequestBody Map<String, String> body) { // Registra usuario vía API
        try {
            String username = body.get("username");
            String nombres = body.get("nombres");
            String apellidos = body.get("apellidos");
            String email = body.get("email");
            String telefono = body.get("telefono");
            String direccion = body.get("direccion");
            String documentoIdentidad = body.get("documentoIdentidad");
            String password = body.get("password");
            String role = body.getOrDefault("role", "USER");

            usuarioService.createUser(
                    username,
                    nombres,
                    apellidos,
                    email,
                    telefono,
                    direccion,
                    documentoIdentidad,
                    password,
                    role
            );

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
    public ResponseEntity<?> listarUsuarios() { // Lista usuarios vía API
        return ResponseEntity.ok(usuarioService.getList());
    }
}