/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.SPA_CACHINA.repositorios;

import com.example.SPA_CACHINA.entidades.Pedido;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author David
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> { // Repositorio para entidad Pedido
    List<Pedido> findByUsuarioIdAndEstadoOrderByFechaPedidoDesc(Long usuarioId, String estado); // Busca por usuario y estado

    List<Pedido> findByUsuarioIdAndEstadoOrderByFechaPedidoAsc(Long usuarioId, String estado); // Busca pedidos de usuario ascendente

    @EntityGraph(attributePaths = "detalles")
    Optional<Pedido> findWithDetallesByIdAndUsuarioIdAndEstado(Long id, Long usuarioId, String estado); // Busca pedido con detalles

    @Query(value = "SELECT p FROM Pedido p LEFT JOIN FETCH p.usuario u WHERE " +
           "(:fechaInicio IS NULL OR p.fechaPedido >= :fechaInicio) AND " +
           "(:fechaFin IS NULL OR p.fechaPedido <= :fechaFin) AND " +
           "(:search IS NULL OR LOWER(u.nombres) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.email) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:estado IS NULL OR p.estado = :estado)",
           countQuery = "SELECT COUNT(p) FROM Pedido p LEFT JOIN p.usuario u WHERE " +
           "(:fechaInicio IS NULL OR p.fechaPedido >= :fechaInicio) AND " +
           "(:fechaFin IS NULL OR p.fechaPedido <= :fechaFin) AND " +
           "(:search IS NULL OR LOWER(u.nombres) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.email) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:estado IS NULL OR p.estado = :estado)")
    Page<Pedido> buscarPedidos(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin, @Param("search") String search, @Param("estado") String estado, Pageable pageable);
}
