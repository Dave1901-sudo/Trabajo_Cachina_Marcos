/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.SPA_CACHINA.repositorios;

import com.example.SPA_CACHINA.entidades.Reservas;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author David
 */
public interface ReservasDAO
                extends JpaRepository<Reservas, Long> {

    @Query("SELECT COUNT(r) FROM Reservas r WHERE r.fecha = ?1 AND r.hora >= ?2 AND r.hora < ?3")
    long countByFechaAndHoraBetween(String fecha, String horaInicio, String horaFin);

    List<Reservas> findByCorreoOrderByFechaAscHoraAsc(String correo);
}