/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.servicios;

import com.example.SPA_CACHINA.Modelo.dto.Reclamaciones;
import com.example.SPA_CACHINA.repositorios.ReclamacionesDAO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author David
 */
@Service
public class ServicioReclamaciones { // Servicio CRUD de reclamaciones
        // Dave

    @Autowired
    private ReclamacionesDAO reclamacionesDAO;

    public List<Reclamaciones> getList() { // Obtiene todas las reclamaciones
        return reclamacionesDAO.findAll();
    }

    public Reclamaciones save(Reclamaciones reclamaciones) { // Guarda o actualiza una reclamación
        return reclamacionesDAO.save(reclamaciones);
    }

    public Reclamaciones get(Long id) { // Obtiene reclamación por ID
        return reclamacionesDAO.findById(id).orElse(null);
    }

    public void delete(Long id) { // Elimina una reclamación
        reclamacionesDAO.deleteById(id);
    }
}
