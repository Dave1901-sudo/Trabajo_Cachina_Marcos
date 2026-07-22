package com.example.SPA_CACHINA.servicios;

import com.example.SPA_CACHINA.entidades.Auditoria;
import com.example.SPA_CACHINA.repositorios.AuditoriaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaService { // Servicio para registro de auditoría

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    public void registrar(String usuario, String accion, String detalle) { // Crea entrada de auditoría
        Auditoria auditoria = new Auditoria(usuario, accion, detalle);
        auditoriaRepository.save(auditoria);
    }

    public List<Auditoria> obtenerTodas() { // Obtiene todos los registros de auditoría
        return auditoriaRepository.findAllByOrderByFechaDesc();
    }
}
