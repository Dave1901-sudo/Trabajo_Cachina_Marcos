/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.servicios;

import com.example.SPA_CACHINA.entidades.Reservas;
import com.example.SPA_CACHINA.repositorios.ReservasDAO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author David
 */
@Service
public class ServicioReservas { // Servicio CRUD de reservas

    @Autowired
    private ReservasDAO reservasDAO;

    public List<Reservas> getList() { // Obtiene todas las reservas
        return reservasDAO.findAll();
    }

    public Reservas save(Reservas reservas) { // Guarda o actualiza una reserva
        return reservasDAO.save(reservas);
    }

    public Reservas get(Long id) { // Obtiene reserva por ID
        return reservasDAO.findById(id).orElse(null);
    }

    public void delete(Long id) { // Elimina una reserva
        reservasDAO.deleteById(id);
    }

    public long contarPorFechaYHora(String fecha, String hora) { // Cuenta reservas para una hora dada
        String horaInicio = hora.split(":")[0] + ":00";
        String horaFin = hora.split(":")[0] + ":59";
        return reservasDAO.countByFechaAndHoraBetween(fecha, horaInicio, horaFin);
    }

    public List<Reservas> obtenerPorCorreo(String correo) { // Obtiene reservas por correo
        return reservasDAO.findByCorreoOrderByFechaAscHoraAsc(correo);
    }
}
