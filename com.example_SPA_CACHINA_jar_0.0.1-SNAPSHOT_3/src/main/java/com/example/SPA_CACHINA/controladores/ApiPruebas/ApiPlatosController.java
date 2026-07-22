/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.controladores.ApiPruebas;

/**
 *
 * @author David
 */
import com.example.SPA_CACHINA.entidades.platos;
import com.example.SPA_CACHINA.servicios.Servicioplatos;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/platos")
public class ApiPlatosController { // API para platos

    @Autowired
    private Servicioplatos servicioplatos;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarPlato(@RequestBody platos plato) { // Registra un plato vía API
        try {
            platos platoGuardado = servicioplatos.save(plato);

            return ResponseEntity.ok(Map.of(
                    "estado", "success",
                    "mensaje", "Plato registrado correctamente",
                    "datos", platoGuardado
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "estado", "error",
                    "mensaje", e.getMessage()
            ));
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarPlatos() { // Lista todos los platos vía API
        return ResponseEntity.ok(servicioplatos.getList());
    }
}