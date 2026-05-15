/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.controladores.ApiPruebas;

/**
 *
 * @author David
 */
import com.example.SPA_CACHINA.entidades.Reservas;
import com.example.SPA_CACHINA.servicios.ServicioReservas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
public class ApiReservasController {

    @Autowired
    private ServicioReservas servicioReservas;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarReserva(@RequestBody Reservas reservas) {

        try {

            servicioReservas.save(reservas);

            return ResponseEntity.ok(Map.of(
                    "estado", "success",
                    "mensaje", "Reserva registrada correctamente",
                    "datos", reservas
            ));

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(Map.of(
                    "estado", "error",
                    "mensaje", e.getMessage()
            ));
        }
    }
    @GetMapping("/listar")
        public ResponseEntity<?> listarReservas() {
            return ResponseEntity.ok(servicioReservas.getList());
        }
}