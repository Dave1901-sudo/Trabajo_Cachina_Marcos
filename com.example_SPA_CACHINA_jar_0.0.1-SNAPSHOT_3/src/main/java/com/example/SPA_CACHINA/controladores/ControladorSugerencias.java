/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.controladores;

import com.example.SPA_CACHINA.entidades.Sugerencias;
import com.example.SPA_CACHINA.servicios.ServicioSugerencias;
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
public class ControladorSugerencias {
    
    @Autowired
    ServicioSugerencias servicioSugerencias;
    
    @GetMapping("/formResultadoSugerencias")
    public String listarSugerencias(Model model) {
        List<Sugerencias> lista = servicioSugerencias.getList();
        model.addAttribute("lista", lista);
        return "formResultadoSugerencias";
    }

    @GetMapping("/sugerencias")
    public String formSugerencias(Model model) {
        model.addAttribute("sugerencias", new Sugerencias());
        return "sugerencias";
    }
    

    @PostMapping("/registrarS")
    public String grabarSugerencias(
            @ModelAttribute Sugerencias sugerencias, Model model) {
        try {
            servicioSugerencias.save(sugerencias);
            model.addAttribute("successMessage", "¡Sugerencia registrada exitosamente!");
            return "sugerencias";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", "Hubo un problema al registrar tu sugerencia. Intenta nuevamente. (Evita ingresar un correo ya existente)");
            model.addAttribute("sugerencias", sugerencias);
            return "sugerencias";
        }

    }
    
    @GetMapping("/getEditS/{codigos}")
    public String editFormSugerencias(Model model,
            @PathVariable("codigos") Long id){
        Sugerencias sugerencias = servicioSugerencias.get(id);
        model.addAttribute("sugerencias", sugerencias);
        return "Sugerencias_edit";
    }
    
    @GetMapping("/deleteS")
    public String deleteFormSugerencias(Model model,
            @RequestParam("id") Long id){
        servicioSugerencias.delete(id);
        return "redirect:/formResultadoSugerencias";
    }

}
