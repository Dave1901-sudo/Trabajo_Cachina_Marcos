package com.example.SPA_CACHINA.controladores;

import com.example.SPA_CACHINA.entidades.Pedido;
import com.example.SPA_CACHINA.entidades.Usuario;
import com.example.SPA_CACHINA.servicios.PedidoService;
import com.example.SPA_CACHINA.servicios.UsuarioService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HistorialPedidosController { // Controlador para la página de historial de pedidos

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/historial-pedidos")
    public String verHistorialPedidos(Model model, Principal principal) { // Muestra historial de pedidos del usuario
        Usuario usuario = usuarioService.getByUsername(principal.getName());

        List<Pedido> pedidosConfirmados = pedidoService.obtenerPedidosConfirmadosPorUsuario(usuario.getId());
        List<Pedido> pedidosPendientes = pedidoService.obtenerPedidosPendientesPorUsuario(usuario.getId());

        model.addAttribute("pedidosConfirmados", pedidosConfirmados);
        model.addAttribute("pedidosPendientes", pedidosPendientes);

        return "historialPedidos";
    }
}
