/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.controladores;

import com.example.SPA_CACHINA.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author David
 */
@Controller
public class LoginController { // Controlador para autenticación

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/register")
    public String showRegisterForm() { // Muestra formulario de registro
        return "register"; // Nombre del archivo Thymeleaf (register.html)
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, // Muestra login con mensajes de error/logout
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        if (error != null && "true".equals(error)) {
            model.addAttribute("errorMessage", "Usuario o clave incorrecto. Por favor intente de nuevo.");
        }
        if (logout != null && "true".equals(logout)) {
            model.addAttribute("logoutMessage", "Ha cerrado su sesión correctamente.");
        }

        System.out.println("Login request -> errorParam: " + error + ", logoutParam: " + logout + ", requestTime: " + System.currentTimeMillis());
        return "login";
    }
    /*
    @PostMapping("/login")
    public String postLogin(@RequestParam String username, @RequestParam String password) {
        // Procesar la autenticación aquí (si es necesario)
        return "redirect:/"; // Redirigir a la página principal tras un inicio de sesión exitoso
    }*/

    @PostMapping("/register")
    public String registerUser( // Registra un nuevo usuario
            @RequestParam String username,
            @RequestParam String nombres,
            @RequestParam String apellidos,
            @RequestParam String email,
            @RequestParam String telefono,
            @RequestParam String direccion,
            @RequestParam String documentoIdentidad,
            @RequestParam String password,
            @RequestParam(name = "role", required = false, defaultValue = "USER") String role,
            Model model) {

        try {

            System.out.println("*****registrar usuario*****");

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

            return "redirect:/login";

        } catch (IllegalArgumentException e) {

            model.addAttribute("errorMessage", e.getMessage());

            return "register";
        }
    }

}
