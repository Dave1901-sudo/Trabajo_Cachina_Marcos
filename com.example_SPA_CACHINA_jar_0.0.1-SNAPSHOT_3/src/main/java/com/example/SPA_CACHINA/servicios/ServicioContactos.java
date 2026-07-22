/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.servicios;

import com.example.SPA_CACHINA.entidades.Contactos;
import com.example.SPA_CACHINA.repositorios.ContactosDAO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author David
 */
@Service
public class ServicioContactos { // Servicio CRUD de contactos
    // Dave

    @Autowired
    private ContactosDAO contactosDAO;

    public List<Contactos> getList() { // Obtiene todos los contactos
        return contactosDAO.findAll();
    }

    public Contactos save(Contactos contactos) { // Guarda o actualiza un contacto
        return contactosDAO.save(contactos);
    }

    public Contactos get(Long id) { // Obtiene contacto por ID
        return contactosDAO.findById(id).orElse(null);
    }

    public void delete(Long id) { // Elimina un contacto
        contactosDAO.deleteById(id);
    }

}
