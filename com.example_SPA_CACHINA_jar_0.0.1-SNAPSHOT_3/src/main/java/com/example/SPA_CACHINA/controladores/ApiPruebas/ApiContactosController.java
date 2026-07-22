/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.controladores.ApiPruebas;

/**
 *
 * @author David
 */
import com.example.SPA_CACHINA.entidades.Contactos;
import com.example.SPA_CACHINA.servicios.ServicioContactos;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contactos")
public class ApiContactosController { // API para contactos

    @Autowired
    private ServicioContactos servicioContactos;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarContacto(@RequestBody Contactos contactos) { // Envía un contacto vía API

        try {

            servicioContactos.save(contactos);

            return ResponseEntity.ok(Map.of(
                    "estado", "success",
                    "mensaje", "Contacto registrado correctamente",
                    "datos", contactos
            ));

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(Map.of(
                    "estado", "error",
                    "mensaje", e.getMessage()
            ));
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarContactos() { // Lista todos los contactos vía API
        return ResponseEntity.ok(servicioContactos.getList());
    }
}