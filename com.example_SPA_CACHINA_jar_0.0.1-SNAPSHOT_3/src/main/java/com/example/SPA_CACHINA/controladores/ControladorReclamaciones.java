/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.controlador;

import com.example.SPA_CACHINA.Modelo.dto.Reclamaciones;
import com.example.SPA_CACHINA.servicios.ServicioReclamaciones;
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
public class ControladorReclamaciones {
    
     @Autowired
    ServicioReclamaciones servicioReclamaciones;

    @GetMapping("/formResultadoReclamaciones")
    public String listarReclamos(Model model) {
        List<Reclamaciones> lista = servicioReclamaciones.getList();
        model.addAttribute("lista", lista);
        return "formResultadoReclamaciones";
    }

    @GetMapping("/reclamaciones")
    public String formReclamos(Model model) {
        model.addAttribute("reclamaciones", new Reclamaciones());
        return "reclamaciones";
    }
    
    
    @PostMapping("/registrarReclamaciones")
    public String grabarReclamos(
            @ModelAttribute Reclamaciones reclamaciones, Model model) {
        try {
            servicioReclamaciones.save(reclamaciones);
            return "reclamaciones";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", e.getMessage().toString());
            model.addAttribute("reclamaciones", reclamaciones);
            return "reclamaciones";
        }

    }
    
    @GetMapping("/getEditReclamaciones/{codigoReclamaciones}")
    public String editFormReclamos(Model model,
            @PathVariable("codigoReclamaciones") Long id){
        Reclamaciones reclamaciones = servicioReclamaciones.get(id);
        model.addAttribute("reclamaciones", reclamaciones);
        return "Reclamaciones_edit";
    }
    
    @GetMapping("/deleteReclamaciones")
    public String deleteFormReclamos(Model model,
            @RequestParam("id") Long id){
        servicioReclamaciones.delete(id);
        return "redirect:/formResultadoReclamaciones";
    }
}
