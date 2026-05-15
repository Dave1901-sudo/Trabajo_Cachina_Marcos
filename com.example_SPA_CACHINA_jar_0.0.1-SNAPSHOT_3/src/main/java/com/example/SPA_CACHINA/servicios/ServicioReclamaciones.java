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
public class ServicioReclamaciones {
        // Dave

    @Autowired
    private ReclamacionesDAO reclamacionesDAO;

    public List<Reclamaciones> getList() {
        return reclamacionesDAO.findAll();
    }

    public Reclamaciones save(Reclamaciones reclamaciones) {
        return reclamacionesDAO.save(reclamaciones);
    }

    public Reclamaciones get(Long id) {
        return reclamacionesDAO.findById(id).orElse(null);
    }

    public void delete(Long id) {
        reclamacionesDAO.deleteById(id);
    }
}
