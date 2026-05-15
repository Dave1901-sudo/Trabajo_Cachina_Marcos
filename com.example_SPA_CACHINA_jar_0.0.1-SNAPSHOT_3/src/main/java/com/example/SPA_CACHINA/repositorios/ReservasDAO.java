/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.SPA_CACHINA.repositorios;

import com.example.SPA_CACHINA.entidades.Reservas;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author David
 */
public interface ReservasDAO
                extends JpaRepository<Reservas, Long> {
    // Dave
}