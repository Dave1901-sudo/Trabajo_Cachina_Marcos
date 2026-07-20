package com.example.SPA_CACHINA.servicios;

import com.example.SPA_CACHINA.entidades.Resena;
import com.example.SPA_CACHINA.entidades.ResenaLike;
import com.example.SPA_CACHINA.repositorios.ResenaLikeRepository;
import com.example.SPA_CACHINA.repositorios.ResenaRepository;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    @Autowired
    private ResenaLikeRepository resenaLikeRepository;

    public List<Resena> obtenerResenasAprobadas(Long platoId) {
        return resenaRepository.findByPlatoIdAndEstadoOrderByLikesDesc(platoId, "aprobado");
    }

    public List<Map<String, Object>> obtenerResenasConLikeState(Long platoId, Long usuarioId) {
        List<Resena> resenas = obtenerResenasAprobadas(platoId);
        if (usuarioId == null) {
            return resenas.stream().map(r -> {
                Map<String, Object> map = new java.util.LinkedHashMap<>();
                map.put("id", r.getId());
                map.put("platoId", r.getPlatoId());
                map.put("usuarioId", r.getUsuarioId());
                map.put("usuarioNombre", r.getUsuarioNombre());
                map.put("puntuacion", r.getPuntuacion());
                map.put("comentario", r.getComentario());
                map.put("fecha", r.getFecha());
                map.put("likes", r.getLikes());
                map.put("estado", r.getEstado());
                map.put("likedByCurrentUser", false);
                return map;
            }).collect(Collectors.toList());
        }
        List<Long> resenaIds = resenas.stream().map(Resena::getId).collect(Collectors.toList());
        List<ResenaLike> likes = resenaLikeRepository.findByResenaIdInAndUsuarioId(resenaIds, usuarioId);
        java.util.Set<Long> likedIds = likes.stream().map(ResenaLike::getResenaId).collect(Collectors.toSet());
        return resenas.stream().map(r -> {
            Map<String, Object> map = new java.util.LinkedHashMap<>();
            map.put("id", r.getId());
            map.put("platoId", r.getPlatoId());
            map.put("usuarioId", r.getUsuarioId());
            map.put("usuarioNombre", r.getUsuarioNombre());
            map.put("puntuacion", r.getPuntuacion());
            map.put("comentario", r.getComentario());
            map.put("fecha", r.getFecha());
            map.put("likes", r.getLikes());
            map.put("estado", r.getEstado());
            map.put("likedByCurrentUser", likedIds.contains(r.getId()));
            return map;
        }).collect(Collectors.toList());
    }

    public Resena crearResena(Long platoId, Long usuarioId, String usuarioNombre, int puntuacion, String comentario) {
        Resena resena = new Resena();
        resena.setPlatoId(platoId);
        resena.setUsuarioId(usuarioId);
        resena.setUsuarioNombre(usuarioNombre);
        resena.setPuntuacion(puntuacion);
        resena.setComentario(comentario);
        resena.setFecha(new Date());
        resena.setLikes(0);
        resena.setEstado("pendiente");
        return resenaRepository.save(resena);
    }

    @Transactional
    public Map<String, Object> darLike(Long resenaId, Long usuarioId) {
        Resena resena = resenaRepository.findById(resenaId)
                .orElseThrow(() -> new RuntimeException("Resena no encontrada"));
        if (usuarioId == null) {
            throw new RuntimeException("Debes iniciar sesion para dar like");
        }
        java.util.Optional<ResenaLike> existing = resenaLikeRepository.findByResenaIdAndUsuarioId(resenaId, usuarioId);
        boolean liked;
        if (existing.isPresent()) {
            resenaLikeRepository.delete(existing.get());
            resena.setLikes(resena.getLikes() - 1);
            liked = false;
        } else {
            ResenaLike like = new ResenaLike();
            like.setResenaId(resenaId);
            like.setUsuarioId(usuarioId);
            resenaLikeRepository.save(like);
            resena.setLikes(resena.getLikes() + 1);
            liked = true;
        }
        resenaRepository.save(resena);
        Map<String, Object> result = new java.util.LinkedHashMap<>();
        result.put("likes", resena.getLikes());
        result.put("liked", liked);
        return result;
    }

    public void aprobarResena(Long resenaId) {
        Resena resena = resenaRepository.findById(resenaId)
                .orElseThrow(() -> new RuntimeException("Resena no encontrada"));
        resena.setEstado("aprobado");
        resenaRepository.save(resena);
    }

    public void eliminarResena(Long resenaId) {
        resenaRepository.deleteById(resenaId);
    }

    public List<Resena> obtenerResenasPendientes() {
        return resenaRepository.findByEstadoOrderByFechaDesc("pendiente");
    }

    public List<Resena> obtenerTodasLasResenas() {
        return resenaRepository.findAll();
    }
}
