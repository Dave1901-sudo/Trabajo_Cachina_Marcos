/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.servicios;

import com.example.SPA_CACHINA.entidades.platos;
import com.example.SPA_CACHINA.repositorios.PlatosDAO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 *
 * @author David
 */
@Service
public class Servicioplatos { // Servicio CRUD de platos
    // Dave

    @Autowired
    private PlatosDAO platosDAO;

    public List<platos> getList() { // Obtiene todos los platos
        return platosDAO.findAll();
    }

    public platos save(platos platos) { // Guarda o actualiza un plato
        return platosDAO.save(platos);
    }

    public platos get(Long id) { // Obtiene plato por ID
        return platosDAO.findById(id).orElse(null);
    }

    public void delete(Long id) { // Elimina un plato
        platosDAO.deleteById(id);
    }

    public List<String> getCategoriasDistinct() { // Obtiene categorías de platos distintas
        return platosDAO.findCategoriasDistinct();
    }

    public platos toggleHabilitado(Long id) { // Alterna estado habilitado/deshabilitado del plato
        platos plato = platosDAO.findById(id).orElse(null);
        if (plato != null) {
            plato.setHabilitado(!plato.isHabilitado());
            return platosDAO.save(plato);
        }
        return null;
    }
}
