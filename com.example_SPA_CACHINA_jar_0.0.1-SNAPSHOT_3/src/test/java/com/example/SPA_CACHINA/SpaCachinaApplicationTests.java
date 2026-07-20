package com.example.SPA_CACHINA;

import com.example.SPA_CACHINA.entidades.*;
import com.example.SPA_CACHINA.locale.PedidoDTO;
import com.example.SPA_CACHINA.locale.PedidoDetalleDTO;
import com.example.SPA_CACHINA.locale.PedidoRequest;
import com.example.SPA_CACHINA.servicios.PedidoMapper;
import com.example.SPA_CACHINA.servicios.PedidoService;
import java.util.Arrays;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpaCachinaApplicationTests {

    @Autowired
    private PedidoMapper pedidoMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void testPedidoMapperToDTO() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setFechaPedido(new Date());
        pedido.setTotal(100.0);
        pedido.setEmail("test@test.com");
        pedido.setPhone("999888777");
        pedido.setDireccion("Av. Test 123");
        pedido.setReferencia("Cerca del parque");
        pedido.setEstado("Pendiente");

        PedidoDetalle detalle = new PedidoDetalle();
        detalle.setId(1L);
        detalle.setNombre("Ceviche");
        detalle.setCantidad(2);
        detalle.setPrecio(25.0);
        detalle.setComentario("Sin cebolla");
        pedido.addDetalle(detalle);

        PedidoDTO dto = pedidoMapper.toDTO(pedido);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(100.0, dto.getTotal());
        assertEquals("test@test.com", dto.getEmail());
        assertEquals("999888777", dto.getPhone());
        assertEquals("Av. Test 123", dto.getDireccion());
        assertEquals("Cerca del parque", dto.getReferencia());
        assertEquals("Pendiente", dto.getEstado());
        assertNotNull(dto.getFechaPedido());

        assertNotNull(dto.getDetalles());
        assertEquals(1, dto.getDetalles().size());
        PedidoDetalleDTO detalleDTO = dto.getDetalles().get(0);
        assertEquals("Ceviche", detalleDTO.getNombre());
        assertEquals(2, detalleDTO.getCantidad());
        assertEquals(25.0, detalleDTO.getPrecio());
        assertEquals("Sin cebolla", detalleDTO.getComentario());
    }

    @Test
    void testPedidoMapperToDTOWithoutUser() {
        Pedido pedido = new Pedido();
        pedido.setId(2L);
        pedido.setTotal(50.0);
        pedido.setEmail("anon@test.com");

        PedidoDTO dto = pedidoMapper.toDTO(pedido);

        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals(50.0, dto.getTotal());
        assertNull(dto.getUsuarioNombre());
        assertNull(dto.getDetalles());
    }
}
