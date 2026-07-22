/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.SPA_CACHINA.repositorios;

import com.example.SPA_CACHINA.Modelo.dto.Reclamaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author David
 */
@Repository
public interface ReclamacionesDAO
        extends JpaRepository<Reclamaciones, Long> { // Repositorio para entidad Reclamaciones
    // Dave
}
