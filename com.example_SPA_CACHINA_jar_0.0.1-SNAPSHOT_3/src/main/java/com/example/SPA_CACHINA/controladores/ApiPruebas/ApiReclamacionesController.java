/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.controladores.ApiPruebas;

/**
 *
 * @author David
 */
import com.example.SPA_CACHINA.Modelo.dto.Reclamaciones;
import com.example.SPA_CACHINA.servicios.ServicioReclamaciones;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reclamaciones")
public class ApiReclamacionesController {

    @Autowired
    private ServicioReclamaciones servicioReclamaciones;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarReclamacion(@RequestBody Reclamaciones reclamaciones) {
        try {
            Reclamaciones reclamacionGuardada = servicioReclamaciones.save(reclamaciones);

            return ResponseEntity.ok(Map.of(
                    "estado", "success",
                    "mensaje", "Reclamación registrada correctamente",
                    "datos", reclamacionGuardada
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "estado", "error",
                    "mensaje", e.getMessage()
            ));
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarReclamaciones() {
        return ResponseEntity.ok(servicioReclamaciones.getList());
    }
}