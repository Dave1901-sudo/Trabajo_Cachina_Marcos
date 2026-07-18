/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.controladores;

import com.example.SPA_CACHINA.entidades.Pedido;
import com.example.SPA_CACHINA.entidades.PedidoDetalle;
import com.example.SPA_CACHINA.servicios.PedidoService;
import com.example.SPA_CACHINA.servicios.Servicioplatos;
import com.example.SPA_CACHINA.servicios.UsuarioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author David
 */
@Controller
@RequestMapping("/pedidos")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private Servicioplatos servicioplatos;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping
    public String listarPedidos(Model model) {
        List<Pedido> pedidos = pedidoService.obtenerTodosLosPedidos();
        model.addAttribute("pedidos", pedidos);
        return "pedidos"; // Vista de la lista de pedidos
    }
    
    @GetMapping("/editar/{id}")
    public String editarPedido(@PathVariable("id") Long id, Model model) {
        Pedido pedido = pedidoService.obtenerPedidoPorId(id);
        model.addAttribute("pedido", pedido);
        model.addAttribute("usuarios", usuarioService.getList());
        return "editarPedido"; // Vista para editar el pedido
    }

    @PostMapping("/editar/{id}")
    public String actualizarPedido(@PathVariable("id") Long id, @ModelAttribute Pedido pedido, @RequestParam("usuarioId") Long usuarioId) {
        pedidoService.actualizarPedido(id, pedido, usuarioId);
        return "redirect:/pedidos"; // Redirige a la lista de pedidos
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPedido(@PathVariable("id") Long id, Model model) {
        model.addAttribute("pedidoId", id); // Pasamos el ID del pedido a la vista
        return "confirmarEliminacion"; // Vista que preguntará por confirmación
    }

    @GetMapping("/eliminarPedido/{id}")
    public String eliminarPedido(@PathVariable("id") Long id) {
        pedidoService.eliminarPedido(id);
        return "redirect:/pedidos"; // Redirige a la lista de pedidos
    }

    @PostMapping("/confirmar/{id}")
    public String confirmarPedido(@PathVariable("id") Long id) {
        pedidoService.confirmarPedido(id); // Marca el pedido como confirmado
        return "redirect:/pedidos"; // Redirige a la lista de pedidos
    }

    @GetMapping("/detalles/{id}")
    public String mostrarDetallesPedido(@PathVariable("id") Long id, Model model) {
        Pedido pedido = pedidoService.obtenerPedidoPorId(id);
        model.addAttribute("pedido", pedido);
        return "detallesPedido";  // Vista que muestra los detalles del pedido
    }

    @GetMapping("/editarDetalle/{id}")
    public String editarDetalle(@PathVariable("id") Long id, Model model) {
        PedidoDetalle detalle = pedidoService.obtenerDetallePorId(id);
        model.addAttribute("detalle", detalle);
        return "editarDetalle"; // Vista para editar el detalle
    }

    @PostMapping("/editarDetalle/{id}")
    public String actualizarDetalle(@PathVariable("id") Long id, @ModelAttribute PedidoDetalle detalle) {
        PedidoDetalle detalleExistente = pedidoService.obtenerDetallePorId(id); // Obtener el detalle actual
        Long pedidoId = detalleExistente.getPedido().getId(); // Obtener el ID del pedido relacionado

        pedidoService.actualizarDetalle(id, detalle);
        return "redirect:/pedidos/detalles/" + pedidoId; // Redirigir al pedido correcto
    }

    // Método para eliminar un detalle de pedido
    @GetMapping("/eliminarDetalle/{id}")
    public String eliminarDetalle(@PathVariable("id") Long id) {
        PedidoDetalle detalleExistente = pedidoService.obtenerDetallePorId(id); // Obtener el detalle existente
        Long pedidoId = detalleExistente.getPedido().getId(); // Obtener el ID del pedido asociado

        pedidoService.eliminarDetalle(id); // Eliminar el detalle
        return "redirect:/pedidos/detalles/" + pedidoId; // Redirigir al pedido correcto
    }
    
     // Mostrar formulario para agregar detalle
    @GetMapping("/agregarDetalle/{pedidoId}")
    public String mostrarFormularioAgregarDetalle(@PathVariable("pedidoId") Long pedidoId, Model model) {
        model.addAttribute("pedidoId", pedidoId);
        model.addAttribute("platos", servicioplatos.getList());
        return "agregarDetalle"; // Vista del formulario
    }

    // Procesar formulario para agregar detalle
    @PostMapping("/agregarDetalle")
    public String agregarDetalle(
            @RequestParam("pedidoId") Long pedidoId,
            @RequestParam("platoId") Long platoId,
            @RequestParam("cantidad") int cantidad,
            @RequestParam("comentario") String comentario) {

        pedidoService.agregarDetalle(pedidoId, platoId, cantidad, comentario);
        return "redirect:/pedidos/detalles/" + pedidoId; // Redirige a los detalles del pedido
    }
    
}
