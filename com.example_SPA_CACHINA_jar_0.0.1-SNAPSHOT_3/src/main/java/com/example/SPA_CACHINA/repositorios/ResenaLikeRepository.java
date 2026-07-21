package com.example.SPA_CACHINA.repositorios;

import com.example.SPA_CACHINA.entidades.ResenaLike;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResenaLikeRepository extends JpaRepository<ResenaLike, Long> {

    Optional<ResenaLike> findByResenaIdAndUsuarioId(Long resenaId, Long usuarioId);

    List<ResenaLike> findByResenaIdInAndUsuarioId(List<Long> resenaIds, Long usuarioId);

    void deleteByResenaIdAndUsuarioId(Long resenaId, Long usuarioId);

    void deleteByResenaId(Long resenaId);
}
