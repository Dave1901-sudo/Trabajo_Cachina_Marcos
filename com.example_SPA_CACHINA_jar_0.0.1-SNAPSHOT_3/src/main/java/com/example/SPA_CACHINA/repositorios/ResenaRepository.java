package com.example.SPA_CACHINA.repositorios;

import com.example.SPA_CACHINA.entidades.Resena;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> { // Repositorio para entidad Resena

    List<Resena> findByPlatoIdAndEstadoOrderByLikesDesc(Long platoId, String estado); // Reseñas aprobadas por plato

    List<Resena> findByEstadoOrderByFechaDesc(String estado); // Reseñas filtradas por estado

    List<Resena> findByPlatoIdOrderByFechaDesc(Long platoId); // Reseñas por plato ordenadas por fecha

    @Query("SELECT r FROM Resena r WHERE " +
           "(:estado IS NULL OR r.estado = :estado) AND " +
           "(:platoId IS NULL OR r.platoId = :platoId) AND " +
           "(:search IS NULL OR LOWER(r.usuarioNombre) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(r.comentario) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "ORDER BY r.fecha DESC")
    List<Resena> buscarResenas(@Param("estado") String estado, @Param("platoId") Long platoId, @Param("search") String search); // Busca reseñas con filtros
}
