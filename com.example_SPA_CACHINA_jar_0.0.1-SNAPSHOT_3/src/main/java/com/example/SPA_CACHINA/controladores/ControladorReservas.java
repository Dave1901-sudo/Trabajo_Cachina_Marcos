/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.controladores;

import com.example.SPA_CACHINA.entidades.Reservas;
import com.example.SPA_CACHINA.servicios.ServicioReservas;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author David
 */
@Controller
public class ControladorReservas {

    @Autowired
    ServicioReservas servicioReservas;

    @GetMapping("/formResultadoReservas")
    public String listarReservas(Model model) {
        List<Reservas> lista = servicioReservas.getList();
        model.addAttribute("lista", lista);
        return "formResultadoReservas";
    }

    @PostMapping("/registrarReservas")
    public String grabarContactos(
            @ModelAttribute Reservas reservas, Model model) {
        try {
            servicioReservas.save(reservas);
            return "redirect:/#reservas";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", e.getMessage().toString());
            model.addAttribute("reservas", reservas);
            return "index";
        }

    }
    
    @GetMapping("/getEditReservas/{codigoReservas}")
    public String editFormContactos(Model model,
            @PathVariable("codigoReservas") Long id){
        Reservas reservas = servicioReservas.get(id);
        model.addAttribute("reservas", reservas);
        return "Reservas_edit";
    }
    
    @GetMapping("/deleteReservas")
    public String deleteFormColaborador(Model model,
            @RequestParam("id") Long id){
        servicioReservas.delete(id);
        return "redirect:/formResultadoReservas";
    }

}
