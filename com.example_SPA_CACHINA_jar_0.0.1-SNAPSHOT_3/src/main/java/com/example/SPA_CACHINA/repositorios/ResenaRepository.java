package com.example.SPA_CACHINA.repositorios;

import com.example.SPA_CACHINA.entidades.Resena;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {

    List<Resena> findByPlatoIdAndEstadoOrderByLikesDesc(Long platoId, String estado);

    List<Resena> findByEstadoOrderByFechaDesc(String estado);

    List<Resena> findByPlatoIdOrderByFechaDesc(Long platoId);
}
