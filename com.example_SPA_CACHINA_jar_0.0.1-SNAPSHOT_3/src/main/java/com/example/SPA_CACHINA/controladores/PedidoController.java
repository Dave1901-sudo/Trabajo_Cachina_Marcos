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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class PedidoController { // Controlador admin para gestión de pedidos
    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private Servicioplatos servicioplatos;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping
    public String listarPedidos( // Lista pedidos con filtros y paginación
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer anio,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String estado,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Date fechaInicio = null;
        Date fechaFin = null;

        if (mes != null && anio != null) {
            Calendar cal = Calendar.getInstance();
            cal.set(anio, mes - 1, 1, 0, 0, 0);
            fechaInicio = cal.getTime();
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            fechaFin = cal.getTime();
        } else if (mes == null && anio == null) {
            Calendar cal = Calendar.getInstance();
            mes = cal.get(Calendar.MONTH) + 1;
            anio = cal.get(Calendar.YEAR);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            fechaInicio = cal.getTime();
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            fechaFin = cal.getTime();
        }

        if (estado == null) estado = "todos";

        String estadoQuery = estado;
        if ("todos".equals(estadoQuery)) estadoQuery = null;

        Sort sort = Sort.by(Sort.Direction.DESC, "fechaPedido").and(Sort.by(Sort.Direction.DESC, "id"));
        Pageable pageable = PageRequest.of(page, 10, sort);
        Page<Pedido> pedidosPage = pedidoService.buscarPedidos(fechaInicio, fechaFin, search, estadoQuery, pageable);

        model.addAttribute("pedidos", pedidosPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pedidosPage.getTotalPages());
        model.addAttribute("totalElements", pedidosPage.getTotalElements());
        model.addAttribute("mes", mes);
        model.addAttribute("anio", anio);
        model.addAttribute("search", search);
        model.addAttribute("estado", estado);
        return "pedidos";
    }
    
    @GetMapping("/editar/{id}")
    public String editarPedido(@PathVariable("id") Long id, Model model) { // Muestra formulario de edición de pedido
        Pedido pedido = pedidoService.obtenerPedidoPorId(id);
        model.addAttribute("pedido", pedido);
        model.addAttribute("usuarios", usuarioService.getList());
        return "editarPedido"; // Vista para editar el pedido
    }

    @PostMapping("/editar/{id}")
    public String actualizarPedido(@PathVariable("id") Long id, @ModelAttribute Pedido pedido, @RequestParam("usuarioId") Long usuarioId) { // Guarda edición de pedido
        pedidoService.actualizarPedido(id, pedido, usuarioId);
        return "redirect:/pedidos"; // Redirige a la lista de pedidos
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPedido(@PathVariable("id") Long id, Model model) { // Muestra confirmación de eliminación
        model.addAttribute("pedidoId", id); // Pasamos el ID del pedido a la vista
        return "confirmarEliminacion"; // Vista que preguntará por confirmación
    }

    @GetMapping("/eliminarPedido/{id}")
    public String eliminarPedido(@PathVariable("id") Long id) { // Elimina pedido permanentemente
        pedidoService.eliminarPedido(id);
        return "redirect:/pedidos"; // Redirige a la lista de pedidos
    }

    @PostMapping("/confirmar/{id}")
    public String confirmarPedido(@PathVariable("id") Long id) { // Alterna estado confirmado/pendiente del pedido
        pedidoService.confirmarPedido(id); // Marca el pedido como confirmado
        return "redirect:/pedidos"; // Redirige a la lista de pedidos
    }

    @GetMapping("/detalles/{id}")
    public String mostrarDetallesPedido(@PathVariable("id") Long id, Model model) { // Muestra detalles del pedido
        Pedido pedido = pedidoService.obtenerPedidoPorId(id);
        model.addAttribute("pedido", pedido);
        return "detallesPedido";  // Vista que muestra los detalles del pedido
    }

    @GetMapping("/editarDetalle/{id}")
    public String editarDetalle(@PathVariable("id") Long id, Model model) { // Muestra formulario de edición de detalle
        PedidoDetalle detalle = pedidoService.obtenerDetallePorId(id);
        model.addAttribute("detalle", detalle);
        return "editarDetalle"; // Vista para editar el detalle
    }

    @PostMapping("/editarDetalle/{id}")
    public String actualizarDetalle(@PathVariable("id") Long id, @ModelAttribute PedidoDetalle detalle) { // Guarda edición de detalle
        PedidoDetalle detalleExistente = pedidoService.obtenerDetallePorId(id); // Obtener el detalle actual
        Long pedidoId = detalleExistente.getPedido().getId(); // Obtener el ID del pedido relacionado

        pedidoService.actualizarDetalle(id, detalle);
        return "redirect:/pedidos/detalles/" + pedidoId; // Redirigir al pedido correcto
    }

    // Método para eliminar un detalle de pedido
    @GetMapping("/eliminarDetalle/{id}")
    public String eliminarDetalle(@PathVariable("id") Long id) { // Elimina un detalle de pedido
        PedidoDetalle detalleExistente = pedidoService.obtenerDetallePorId(id); // Obtener el detalle existente
        Long pedidoId = detalleExistente.getPedido().getId(); // Obtener el ID del pedido asociado

        pedidoService.eliminarDetalle(id); // Eliminar el detalle
        return "redirect:/pedidos/detalles/" + pedidoId; // Redirigir al pedido correcto
    }
    
     // Mostrar formulario para agregar detalle
     @GetMapping("/agregarDetalle/{pedidoId}")
    public String mostrarFormularioAgregarDetalle(@PathVariable("pedidoId") Long pedidoId, Model model) { // Muestra formulario para agregar detalle
        model.addAttribute("pedidoId", pedidoId);
        model.addAttribute("platos", servicioplatos.getList());
        return "agregarDetalle"; // Vista del formulario
    }

    // Procesar formulario para agregar detalle
    @PostMapping("/agregarDetalle")
    public String agregarDetalle( // Agrega una nueva línea de detalle a un pedido
            @RequestParam("pedidoId") Long pedidoId,
            @RequestParam("platoId") Long platoId,
            @RequestParam("cantidad") int cantidad,
            @RequestParam("comentario") String comentario) {

        pedidoService.agregarDetalle(pedidoId, platoId, cantidad, comentario);
        return "redirect:/pedidos/detalles/" + pedidoId; // Redirige a los detalles del pedido
    }
    
}
