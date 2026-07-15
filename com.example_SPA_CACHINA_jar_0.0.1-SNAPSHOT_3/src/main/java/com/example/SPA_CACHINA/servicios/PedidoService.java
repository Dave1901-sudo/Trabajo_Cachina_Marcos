/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.servicios;

import com.example.SPA_CACHINA.entidades.Pedido;
import com.example.SPA_CACHINA.entidades.PedidoDetalle;
import com.example.SPA_CACHINA.entidades.platos;
import com.example.SPA_CACHINA.locale.CarritoItem;
import com.example.SPA_CACHINA.locale.PedidoRequest;
import com.example.SPA_CACHINA.locale.ResponseMessage;
import com.example.SPA_CACHINA.repositorios.PedidoDetalleRepository;
import com.example.SPA_CACHINA.repositorios.PedidoRepository;
import com.example.SPA_CACHINA.repositorios.PlatosDAO;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository; // Repositorio para la entidad Pedido

    @Autowired
    private BrevoService brevoService;

    @Autowired
    private PedidoDetalleRepository pedidoDetalleRepository;
    @Autowired
    private PlatosDAO platosDAO;
    
    public ResponseMessage guardarPedido(PedidoRequest orderData) {
        try {
            // Crear un objeto Pedido
            Pedido pedido = new Pedido();
            pedido.setFechaPedido(new Date());
            pedido.setEmail(orderData.getEmail());
            pedido.setPhone(orderData.getPhone());
            pedido.setDireccion(orderData.getDireccion());

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
            // Enviar correo
            enviarCorreo(orderData.getEmail(), total, pedido.getDetalles());
            enviarNotificacionNuevoPedido();
            
            return new ResponseMessage("Pedido realizado con éxito.");
        } catch (Exception e) {
            return new ResponseMessage("Hubo un error al realizar el pedido.");
        }
    }

    // Nuevo método para obtener todos los pedidos
    public List<Pedido> obtenerTodosLosPedidos() {
        return pedidoRepository.findAll(); // Recupera todos los pedidos de la base de datos
    }

    // Método para obtener un pedido por su ID
    public Pedido obtenerPedidoPorId(Long id) {
        return pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    // Método para actualizar un pedido
    public void actualizarPedido(Long id, Pedido pedidoActualizado) {
        Pedido pedido = obtenerPedidoPorId(id);
        pedido.setFechaPedido(pedidoActualizado.getFechaPedido());
        pedido.setTotal(pedidoActualizado.getTotal());
        pedido.setEmail(pedidoActualizado.getEmail());
        pedido.setPhone(pedidoActualizado.getPhone());
        pedido.setDireccion(pedidoActualizado.getDireccion());
        // Actualizar otros campos si es necesario
        pedidoRepository.save(pedido);
    }

    // Método para eliminar un pedido
    public void eliminarPedido(Long id) {
        Pedido pedido = obtenerPedidoPorId(id);
        pedidoRepository.delete(pedido);
    }

    // Método para confirmar un pedido
    public void confirmarPedido(Long id) {
        Pedido pedido = obtenerPedidoPorId(id);
        if ("Pendiente".equals(pedido.getEstado())) {
            pedido.setEstado("Confirmado");
        } else if ("Confirmado".equals(pedido.getEstado())) {
            pedido.setEstado("Pendiente");
        } // Puedes cambiar el estado de acuerdo a tu modelo
        pedidoRepository.save(pedido);
    }

    // Método para obtener un detalle de pedido por ID
    public PedidoDetalle obtenerDetallePorId(Long id) {
        return pedidoDetalleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));
    }

    // Método para actualizar un detalle de pedido
    public void actualizarDetalle(Long id, PedidoDetalle detalleActualizado) {
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

    public void eliminarDetalle(Long id) {
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


   public void agregarDetalle(Long pedidoId, Long platoId, int cantidad, String comentario) {
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

    
    private void enviarCorreo(String email, double total, List<PedidoDetalle> detallesPedido) {
        try {
            // Cargar la plantilla HTML
            String template = new String(Files.readAllBytes(Paths.get("src/main/resources/templates/pedido-confirmacion.html")));

            // Reemplazar los datos en la plantilla
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
    
    public void enviarNotificacionNuevoPedido() {

        String contenido =
                "<h3>Nuevo Pedido Realizado</h3>" +
                "<p>Un nuevo pedido ha sido realizado. Revisa los detalles del pedido en el sistema.</p>";

        brevoService.enviarCorreo(
                "u22244804@utp.edu.pe",
                "Notificación de Nuevo Pedido",
                contenido
        );

    }

}
