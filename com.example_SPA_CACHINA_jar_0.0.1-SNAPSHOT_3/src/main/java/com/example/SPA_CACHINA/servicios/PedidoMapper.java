package com.example.SPA_CACHINA.servicios;

import com.example.SPA_CACHINA.entidades.Pedido;
import com.example.SPA_CACHINA.entidades.PedidoDetalle;
import com.example.SPA_CACHINA.locale.PedidoDTO;
import com.example.SPA_CACHINA.locale.PedidoDetalleDTO;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class PedidoMapper { // Mapea entidad Pedido a PedidoDTO

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public PedidoDTO toDTO(Pedido pedido) { // Convierte Pedido a DTO
        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        dto.setFechaPedido(pedido.getFechaPedido() != null ? DATE_FORMAT.format(pedido.getFechaPedido()) : null);
        dto.setTotal(pedido.getTotal());
        dto.setEmail(pedido.getEmail());
        dto.setPhone(pedido.getPhone());
        dto.setDireccion(pedido.getDireccion());
        dto.setReferencia(pedido.getReferencia());
        dto.setEstado(pedido.getEstado());
        if (pedido.getUsuario() != null) {
            dto.setUsuarioNombre(pedido.getUsuario().getNombres() + " " + pedido.getUsuario().getApellidos());
        }
        if (pedido.getDetalles() != null) {
            dto.setDetalles(pedido.getDetalles().stream().map(this::toDetalleDTO).collect(Collectors.toList()));
        }
        return dto;
    }

    public List<PedidoDTO> toDTOList(List<Pedido> pedidos) { // Convierte lista de Pedidos a DTOs
        return pedidos.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private PedidoDetalleDTO toDetalleDTO(PedidoDetalle detalle) { // Convierte PedidoDetalle a DTO
        PedidoDetalleDTO dto = new PedidoDetalleDTO();
        dto.setId(detalle.getId());
        dto.setNombre(detalle.getNombre());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecio(detalle.getPrecio());
        dto.setComentario(detalle.getComentario());
        return dto;
    }
}
