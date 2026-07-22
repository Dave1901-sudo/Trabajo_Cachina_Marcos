package com.example.SPA_CACHINA.repositorios;

import com.example.SPA_CACHINA.entidades.ResenaLike;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResenaLikeRepository extends JpaRepository<ResenaLike, Long> { // Repositorio para likes de reseñas

    Optional<ResenaLike> findByResenaIdAndUsuarioId(Long resenaId, Long usuarioId); // Busca like por reseña y usuario

    List<ResenaLike> findByResenaIdInAndUsuarioId(List<Long> resenaIds, Long usuarioId); // Busca likes para múltiples reseñas

    void deleteByResenaIdAndUsuarioId(Long resenaId, Long usuarioId); // Elimina like por reseña y usuario

    void deleteByResenaId(Long resenaId); // Elimina todos los likes de una reseña
}
