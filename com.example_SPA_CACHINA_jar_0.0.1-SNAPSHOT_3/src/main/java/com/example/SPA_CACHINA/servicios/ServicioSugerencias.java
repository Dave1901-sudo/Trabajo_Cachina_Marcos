/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.servicios;

import com.example.SPA_CACHINA.entidades.Sugerencias;
import com.example.SPA_CACHINA.repositorios.SugerenciasDAO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author David
 */
@Service
public class ServicioSugerencias { // Servicio CRUD de sugerencias
    // Dave

    @Autowired
    private SugerenciasDAO sugerenciasDAO;

    public List<Sugerencias> getList() { // Obtiene todas las sugerencias
        return sugerenciasDAO.findAll();
    }

    public Sugerencias save(Sugerencias sugerencias) { // Guarda o actualiza una sugerencia
        return sugerenciasDAO.save(sugerencias);
    }

    public Sugerencias get(Long id) { // Obtiene sugerencia por ID
        return sugerenciasDAO.findById(id).orElse(null);
    }

    public void delete(Long id) { // Elimina una sugerencia
        sugerenciasDAO.deleteById(id);
    }
}
