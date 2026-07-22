/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.controladores.ApiPruebas;

/**
 *
 * @author David
 */
import com.example.SPA_CACHINA.locale.PedidoRequest;
import com.example.SPA_CACHINA.locale.ResponseMessage;
import com.example.SPA_CACHINA.servicios.PedidoService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class ApiPedidosController { // API para pedidos

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarPedido(@RequestBody PedidoRequest pedidoRequest) { // Registra un pedido vía API
        try {
            ResponseMessage respuesta = pedidoService.guardarPedido(pedidoRequest);

            return ResponseEntity.ok(Map.of(
                    "estado", "success",
                    "mensaje", respuesta.getMessage()
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "estado", "error",
                    "mensaje", e.getMessage()
            ));
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarPedidos() { // Lista todos los pedidos vía API
        return ResponseEntity.ok(pedidoService.obtenerTodosLosPedidos());
    }
}
