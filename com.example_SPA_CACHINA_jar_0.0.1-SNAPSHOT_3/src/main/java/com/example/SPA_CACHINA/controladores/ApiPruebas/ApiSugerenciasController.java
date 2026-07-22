/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.controladores.ApiPruebas;

/**
 *
 * @author David
 */
import com.example.SPA_CACHINA.entidades.Sugerencias;
import com.example.SPA_CACHINA.servicios.ServicioSugerencias;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sugerencias")
public class ApiSugerenciasController { // API para sugerencias

    @Autowired
    private ServicioSugerencias servicioSugerencias;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarSugerencia(@RequestBody Sugerencias sugerencias) { // Envía sugerencia vía API
        try {
            Sugerencias sugerenciaGuardada = servicioSugerencias.save(sugerencias);

            return ResponseEntity.ok(Map.of(
                    "estado", "success",
                    "mensaje", "Sugerencia registrada correctamente",
                    "datos", sugerenciaGuardada
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "estado", "error",
                    "mensaje", e.getMessage()
            ));
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarSugerencias() { // Lista sugerencias vía API
        return ResponseEntity.ok(servicioSugerencias.getList());
    }
}
