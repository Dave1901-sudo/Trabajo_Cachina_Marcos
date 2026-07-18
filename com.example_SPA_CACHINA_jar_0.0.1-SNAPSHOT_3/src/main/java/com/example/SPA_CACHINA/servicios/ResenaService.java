package com.example.SPA_CACHINA.servicios;

import com.example.SPA_CACHINA.entidades.Resena;
import com.example.SPA_CACHINA.repositorios.ResenaRepository;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    public List<Resena> obtenerResenasAprobadas(Long platoId) {
        List<Resena> resenas = resenaRepository.findByPlatoIdAndEstadoOrderByLikesDesc(platoId, "aprobado");
        return resenas.size() > 3 ? resenas.subList(0, 3) : resenas;
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

    public void darLike(Long resenaId) {
        Resena resena = resenaRepository.findById(resenaId)
                .orElseThrow(() -> new RuntimeException("Resena no encontrada"));
        resena.setLikes(resena.getLikes() + 1);
        resenaRepository.save(resena);
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
