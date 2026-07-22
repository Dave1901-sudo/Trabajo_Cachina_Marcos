/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.controladores.ApiPruebas;

/**
 *
 * @author David
 */
import com.example.SPA_CACHINA.entidades.Usuario;
import com.example.SPA_CACHINA.repositorios.UserRepository;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class ApiLoginController { // API para validación de login

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/validar")
    public ResponseEntity<?> validarLogin(@RequestBody Map<String, String> body) { // Valida credenciales vía API
        String username = body.get("username");
        String password = body.get("password");

        Usuario usuario = userRepository.findByUsername(username);

        if (usuario == null) {
            return ResponseEntity.status(401).body(Map.of(
                "estado", "error",
                "mensaje", "Usuario no encontrado"
            ));
        }

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            return ResponseEntity.status(401).body(Map.of(
                "estado", "error",
                "mensaje", "Contraseña incorrecta"
            ));
        }

        return ResponseEntity.ok(Map.of(
            "estado", "success",
            "mensaje", "Login correcto",
            "username", usuario.getUsername(),
            "role", usuario.getRole()
        ));
    }

    @GetMapping("/verificar")
    public ResponseEntity<?> verificarApiLogin() { // Verifica disponibilidad de API login
        return ResponseEntity.ok(Map.of(
            "estado", "success",
            "mensaje", "API de login disponible"
        ));
    }
}