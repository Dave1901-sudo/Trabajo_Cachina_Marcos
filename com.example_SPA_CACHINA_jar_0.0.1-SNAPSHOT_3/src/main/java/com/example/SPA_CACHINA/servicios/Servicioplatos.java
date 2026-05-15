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
public class Servicioplatos {
    // Dave

    @Autowired
    private PlatosDAO platosDAO;

    public List<platos> getList() {
        return platosDAO.findAll();
    }

    public platos save(platos platos) {
        return platosDAO.save(platos);
    }

    public platos get(Long id) {
        return platosDAO.findById(id).orElse(null);
    }

    public void delete(Long id) {
        platosDAO.deleteById(id);
    }
}
