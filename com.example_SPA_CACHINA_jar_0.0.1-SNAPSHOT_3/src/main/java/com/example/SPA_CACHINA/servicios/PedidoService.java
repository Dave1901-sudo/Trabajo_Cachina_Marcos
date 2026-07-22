/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.servicios;

import com.example.SPA_CACHINA.entidades.Pedido;
import com.example.SPA_CACHINA.entidades.PedidoDetalle;
import com.example.SPA_CACHINA.entidades.Usuario;
import com.example.SPA_CACHINA.entidades.platos;
import com.example.SPA_CACHINA.locale.CarritoItem;
import com.example.SPA_CACHINA.locale.PedidoRequest;
import com.example.SPA_CACHINA.locale.ResponseMessage;
import com.example.SPA_CACHINA.repositorios.PedidoDetalleRepository;
import com.example.SPA_CACHINA.repositorios.PedidoRepository;
import com.example.SPA_CACHINA.repositorios.PlatosDAO;
import com.example.SPA_CACHINA.repositorios.UserRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author David
 */
@Service
public class PedidoService { // Servicio para lógica de negocio de pedidos

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private BrevoService brevoService;

    @Autowired
    private PedidoDetalleRepository pedidoDetalleRepository;
    @Autowired
    private PlatosDAO platosDAO;
    
    @Autowired
    private UserRepository userRepository;
    
    public ResponseMessage guardarPedido(PedidoRequest orderData) { // Guarda pedido sin sesión de usuario
        return guardarPedido(orderData, null);
    }

    public ResponseMessage guardarPedido(PedidoRequest orderData, Usuario usuario) { // Guarda pedido con usuario y envía correos
        try {
            // Crear un objeto Pedido
            Pedido pedido = new Pedido();
            pedido.setFechaPedido(new Date());
            pedido.setEmail(orderData.getEmail());
            pedido.setPhone(orderData.getPhone());
            pedido.setDireccion(orderData.getDireccion());
            pedido.setReferencia(orderData.getReferencia());
            pedido.setUsuario(usuario);

            double total = 0;
            
            for (CarritoItem item : orderData.getOrderItems()) {
                PedidoDetalle detalle = new PedidoDetalle();
                detalle.setPlatoId(item.getIdPlato());
                detalle.setNombre(item.getNombre());
                detalle.setCantidad(item.getCantidad());
                detalle.setPrecio(item.getPrecio());
                detalle.setComentario(item.getComentario());

                pedido.addDetalle(detalle);
                total += detalle.getPrecio() * detalle.getCantidad();
            }

                        // Redondear el total a 2 decimales antes de asignarlo al pedido
            BigDecimal totalRedondeado = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP);
            pedido.setTotal(totalRedondeado.doubleValue());
           
            pedidoRepository.save(pedido);

            // Enviar correo
            String nombresCorreo = (usuario != null) ? usuario.getNombres() + " " + usuario.getApellidos() : orderData.getNombres();
            enviarCorreo(orderData.getEmail(), nombresCorreo, orderData.getPhone(), orderData.getDireccion(), orderData.getReferencia(), total, pedido.getDetalles());
            enviarNotificacionNuevoPedido(usuario, pedido);
            
            return new ResponseMessage("Pedido realizado con éxito.");
        } catch (Exception e) {
            return new ResponseMessage("Hubo un error al realizar el pedido.");
        }
    }

    // Nuevo método para obtener todos los pedidos
    public List<Pedido> obtenerTodosLosPedidos() { // Obtiene todos los pedidos
        return pedidoRepository.findAll();
    }

    public Page<Pedido> buscarPedidos(Date fechaInicio, Date fechaFin, String search, String estado, Pageable pageable) { // Busca pedidos con filtros
        return pedidoRepository.buscarPedidos(fechaInicio, fechaFin, search, estado, pageable);
    }

    public List<Pedido> obtenerPedidosPendientesPorUsuario(Long usuarioId) { // Obtiene pedidos pendientes del usuario
        return pedidoRepository.findByUsuarioIdAndEstadoOrderByFechaPedidoAsc(usuarioId, "Pendiente");
    }

    public List<Pedido> obtenerPedidosConfirmadosPorUsuario(Long usuarioId) { // Obtiene pedidos confirmados del usuario
        return pedidoRepository.findByUsuarioIdAndEstadoOrderByFechaPedidoAsc(usuarioId, "Confirmado");
    }

    public Pedido obtenerPedidoPendientePorIdYUsuario(Long id, Long usuarioId) { // Busca pedido pendiente por ID para usuario
        return pedidoRepository.findWithDetallesByIdAndUsuarioIdAndEstado(id, usuarioId, "Pendiente")
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    // Método para obtener un pedido por su ID
    public Pedido obtenerPedidoPorId(Long id) { // Obtiene pedido por ID
        return pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    // Método para actualizar un pedido
    public void actualizarPedido(Long id, Pedido pedidoActualizado, Long usuarioId) { // Actualiza campos del pedido
        Pedido pedido = obtenerPedidoPorId(id);
        pedido.setFechaPedido(pedidoActualizado.getFechaPedido());
        pedido.setTotal(pedidoActualizado.getTotal());
        pedido.setEmail(pedidoActualizado.getEmail());
        pedido.setPhone(pedidoActualizado.getPhone());
        pedido.setDireccion(pedidoActualizado.getDireccion());
        if (usuarioId != null) {
            Usuario usuario = userRepository.findById(usuarioId).orElse(null);
            pedido.setUsuario(usuario);
        }
        pedidoRepository.save(pedido);
    }

    // Método para eliminar un pedido
    public void eliminarPedido(Long id) { // Elimina un pedido
        Pedido pedido = obtenerPedidoPorId(id);
        pedidoRepository.delete(pedido);
    }

    // Método para confirmar un pedido
    public void confirmarPedido(Long id) { // Alterna pedido entre pendiente y confirmado
        Pedido pedido = obtenerPedidoPorId(id);
        if ("Pendiente".equals(pedido.getEstado())) {
            pedido.setEstado("Confirmado");
        } else if ("Confirmado".equals(pedido.getEstado())) {
            pedido.setEstado("Pendiente");
        } // Puedes cambiar el estado de acuerdo a tu modelo
        pedidoRepository.save(pedido);
    }

    // Método para obtener un detalle de pedido por ID
    public PedidoDetalle obtenerDetallePorId(Long id) { // Obtiene detalle de pedido por ID
        return pedidoDetalleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));
    }

    // Método para actualizar un detalle de pedido
    public void actualizarDetalle(Long id, PedidoDetalle detalleActualizado) { // Actualiza detalle y recalcula total
        PedidoDetalle detalle = obtenerDetallePorId(id);
        detalle.setCantidad(detalleActualizado.getCantidad());
        detalle.setPrecio(detalleActualizado.getPrecio());
        detalle.setComentario(detalleActualizado.getComentario());
        pedidoDetalleRepository.save(detalle); // Guardar el detalle actualizado

        Pedido pedido = detalle.getPedido(); // Obtener el pedido asociado al detalle
        double nuevoTotal = 0;
        for (PedidoDetalle d : pedido.getDetalles()) {
            nuevoTotal += d.getCantidad() * d.getPrecio(); // Sumar el total de cada detalle
        }

        // Redondear el nuevo total a 2 decimales antes de asignarlo al pedido
        BigDecimal nuevoTotalRedondeado = new BigDecimal(nuevoTotal).setScale(2, RoundingMode.HALF_UP);
        pedido.setTotal(nuevoTotalRedondeado.doubleValue()); // Asignar el nuevo total al pedido

        pedidoRepository.save(pedido); // Guardar el pedido actualizado
    }

    public void eliminarDetalle(Long id) { // Elimina detalle y recalcula total
    // Obtener el detalle a eliminar
    PedidoDetalle detalle = pedidoDetalleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

    // Obtener el pedido asociado al detalle
    Pedido pedido = detalle.getPedido();

    // Eliminar el detalle
    pedidoDetalleRepository.delete(detalle);

    // Recalcular el total del pedido
    double nuevoTotal = 0;
    for (PedidoDetalle d : pedido.getDetalles()) {
        if (!d.getId().equals(id)) { // Excluir el detalle eliminado
            nuevoTotal += d.getCantidad() * d.getPrecio();
        }
    }

    // Redondear el nuevo total a 2 decimales
    BigDecimal nuevoTotalRedondeado = new BigDecimal(nuevoTotal).setScale(2, RoundingMode.HALF_UP);
    pedido.setTotal(nuevoTotalRedondeado.doubleValue());

    // Guardar el pedido con el nuevo total
    pedidoRepository.save(pedido);
}


   public void agregarDetalle(Long pedidoId, Long platoId, int cantidad, String comentario) { // Agrega línea de detalle y recalcula total
    // Obtener el pedido
    Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

    // Obtener el plato
    platos plato = platosDAO.findById(platoId)
            .orElseThrow(() -> new RuntimeException("Plato no encontrado"));

    // Crear un nuevo detalle
    PedidoDetalle nuevoDetalle = new PedidoDetalle();
    nuevoDetalle.setPedido(pedido);
    nuevoDetalle.setNombre(plato.getNombre());
    nuevoDetalle.setPlatoId(plato.getIdplato());
    nuevoDetalle.setCantidad(cantidad);
    nuevoDetalle.setPrecio(plato.getPrecio());
    nuevoDetalle.setComentario(comentario);

    // Guardar el detalle
    pedidoDetalleRepository.save(nuevoDetalle);

    // Recalcular el total del pedido
    double nuevoTotal = 0;
    for (PedidoDetalle detalle : pedido.getDetalles()) {
        nuevoTotal += detalle.getCantidad() * detalle.getPrecio();
    }

    // Redondear el nuevo total a 2 decimales
    BigDecimal nuevoTotalRedondeado = new BigDecimal(nuevoTotal).setScale(2, RoundingMode.HALF_UP);
    pedido.setTotal(nuevoTotalRedondeado.doubleValue());

    // Guardar el pedido con el nuevo total
    pedidoRepository.save(pedido);
}

    
    private void enviarCorreo(String email, String nombres, String telefono, String direccion, String referencia, double total, List<PedidoDetalle> detallesPedido) { // Envía correo de confirmación de pedido
        try {
            String template = new String(Files.readAllBytes(Paths.get("src/main/resources/templates/pedido-confirmacion.html")));

            StringBuilder detallesHtml = new StringBuilder();
            for (PedidoDetalle detalle : detallesPedido) {
                detallesHtml.append("<tr>")
                        .append("<td>").append(detalle.getNombre()).append("</td>")
                        .append("<td>").append(detalle.getCantidad()).append("</td>")
                        .append("<td>S/. ").append(String.format("%.2f", detalle.getPrecio())).append("</td>")
                         .append("<td>S/. ").append(String.format("%.2f", detalle.getCantidad() * detalle.getPrecio())).append("</td>")
                        .append("</tr>");
            }

            String contenido = template
                    .replace("{{nombres}}", nombres != null ? nombres : "")
                    .replace("{{telefono}}", telefono != null ? telefono : "")
                    .replace("{{direccion}}", direccion != null ? direccion : "")
                    .replace("{{referencia}}", referencia != null && !referencia.isEmpty() ? referencia : "Ninguna referencia asignada")
                    .replace("{{total}}", String.format("%.2f", total))
                    .replace("<!-- Detalles del pedido serán insertados aquí -->", detallesHtml.toString());

            brevoService.enviarCorreo(
                    email,
                    "Confirmación de Pedido",
                    contenido
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
    
    public void cancelarPedido(Long id, Long usuarioId) { // Cancela pedido pendiente dentro de 5 min y notifica
        Pedido pedido = pedidoRepository.findWithDetallesByIdAndUsuarioIdAndEstado(id, usuarioId, "Pendiente")
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado o no se puede cancelar"));

        long diffMs = new Date().getTime() - pedido.getFechaPedido().getTime();
        long diffMin = diffMs / 60000;
        if (diffMin > 5) {
            throw new RuntimeException("El tiempo para cancelar el pedido ha expirado (5 minutos).");
        }

        Usuario usuario = pedido.getUsuario();
        String nombres = (usuario != null) ? usuario.getNombres() + " " + usuario.getApellidos() : "Usuario anónimo";

        String template;
        try {
            template = new String(Files.readAllBytes(Paths.get("src/main/resources/templates/pedido-cancelacion.html")));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cargar la plantilla de cancelación");
        }

        StringBuilder detallesHtml = new StringBuilder();
        if (pedido.getDetalles() != null) {
            for (PedidoDetalle detalle : pedido.getDetalles()) {
                detallesHtml.append("<tr>")
                        .append("<td>").append(detalle.getNombre()).append("</td>")
                        .append("<td>").append(detalle.getCantidad()).append("</td>")
                        .append("<td>S/. ").append(String.format("%.2f", detalle.getPrecio())).append("</td>")
                        .append("<td>S/. ").append(String.format("%.2f", detalle.getCantidad() * detalle.getPrecio())).append("</td>")
                        .append("</tr>");
            }
        }

        String contenido = template
                .replace("{{nombres}}", nombres != null ? nombres : "")
                .replace("{{email}}", pedido.getEmail() != null ? pedido.getEmail() : "")
                .replace("{{telefono}}", pedido.getPhone() != null ? pedido.getPhone() : "")
                .replace("{{direccion}}", pedido.getDireccion() != null ? pedido.getDireccion() : "")
                .replace("{{referencia}}", pedido.getReferencia() != null && !pedido.getReferencia().isEmpty() ? pedido.getReferencia() : "Ninguna referencia asignada")
                .replace("{{total}}", String.format("%.2f", pedido.getTotal()))
                .replace("<!-- Detalles del pedido serán insertados aquí -->", detallesHtml.toString());

        brevoService.enviarCorreo(
                "u22244804@utp.edu.pe",
                "Notificación de Cancelación de Pedido",
                contenido
        );

        pedidoRepository.delete(pedido);
    }

    public void enviarNotificacionNuevoPedido(Usuario usuario, Pedido pedido) { // Envía notificación de nuevo pedido al admin
        String nombres = (usuario != null) ? usuario.getNombres() + " " + usuario.getApellidos() : "Usuario anónimo";

        String template;
        try {
            template = new String(Files.readAllBytes(Paths.get("src/main/resources/templates/pedido-nuevo.html")));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cargar la plantilla de nuevo pedido");
        }

        StringBuilder detallesHtml = new StringBuilder();
        if (pedido.getDetalles() != null) {
            for (PedidoDetalle detalle : pedido.getDetalles()) {
                detallesHtml.append("<tr>")
                        .append("<td>").append(detalle.getNombre()).append("</td>")
                        .append("<td>").append(detalle.getCantidad()).append("</td>")
                        .append("<td>S/. ").append(String.format("%.2f", detalle.getPrecio())).append("</td>")
                        .append("<td>S/. ").append(String.format("%.2f", detalle.getCantidad() * detalle.getPrecio())).append("</td>")
                        .append("</tr>");
            }
        }

        String contenido = template
                .replace("{{nombres}}", nombres != null ? nombres : "")
                .replace("{{email}}", pedido.getEmail() != null ? pedido.getEmail() : "")
                .replace("{{telefono}}", pedido.getPhone() != null ? pedido.getPhone() : "")
                .replace("{{direccion}}", pedido.getDireccion() != null ? pedido.getDireccion() : "")
                .replace("{{referencia}}", pedido.getReferencia() != null && !pedido.getReferencia().isEmpty() ? pedido.getReferencia() : "Ninguna referencia asignada")
                .replace("{{total}}", String.format("%.2f", pedido.getTotal()))
                .replace("<!-- Detalles del pedido serán insertados aquí -->", detallesHtml.toString());

        brevoService.enviarCorreo(
                "u22244804@utp.edu.pe",
                "Notificación de Nuevo Pedido",
                contenido
        );

    }

}
