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
public class ServicioReservas {

    @Autowired
    private ReservasDAO reservasDAO;

    public List<Reservas> getList() {
        return reservasDAO.findAll();
    }

    public Reservas save(Reservas reservas) {
        return reservasDAO.save(reservas);
    }

    public Reservas get(Long id) {
        return reservasDAO.findById(id).orElse(null);
    }

    public void delete(Long id) {
        reservasDAO.deleteById(id);
    }

    public long contarPorFechaYHora(String fecha, String hora) {
        String horaInicio = hora.split(":")[0] + ":00";
        String horaFin = hora.split(":")[0] + ":59";
        return reservasDAO.countByFechaAndHoraBetween(fecha, horaInicio, horaFin);
    }

    public List<Reservas> obtenerPorCorreo(String correo) {
        return reservasDAO.findByCorreoOrderByFechaAscHoraAsc(correo);
    }
}
