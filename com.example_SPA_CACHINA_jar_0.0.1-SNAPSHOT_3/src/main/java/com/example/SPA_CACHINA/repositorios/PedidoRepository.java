/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.SPA_CACHINA.repositorios;

import com.example.SPA_CACHINA.entidades.Pedido;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author David
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioIdAndEstadoOrderByFechaPedidoDesc(Long usuarioId, String estado);

    @EntityGraph(attributePaths = "detalles")
    Optional<Pedido> findWithDetallesByIdAndUsuarioIdAndEstado(Long id, Long usuarioId, String estado);
}
