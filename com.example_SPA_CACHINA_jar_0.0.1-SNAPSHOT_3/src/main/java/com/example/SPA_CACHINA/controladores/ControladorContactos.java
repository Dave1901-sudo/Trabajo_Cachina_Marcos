package com.example.SPA_CACHINA.controladores;

import com.example.SPA_CACHINA.entidades.Contactos;
import com.example.SPA_CACHINA.servicios.ServicioContactos;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ControladorContactos {

    @Autowired
    ServicioContactos servicioContactos;

    @GetMapping("/formResultadoContactos")
    public String listarContactos(Model model) {
        List<Contactos> lista = servicioContactos.getList();
        model.addAttribute("lista", lista);
        return "formResultadoContactos";
    }

    @GetMapping("/contactos")
    public String formContactos(Model model) {
        model.addAttribute("contactos", new Contactos());
        return "contactos";
    }
    

    @PostMapping("/registrar")
    public String grabarContactos(
            @ModelAttribute Contactos contactos, Model model) {
        try {
            servicioContactos.save(contactos);
            model.addAttribute("successMessage", "¡Sugerencia registrada exitosamente!");
            return "contactos";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", "Hubo un problema al registrar tu sugerencia. Intenta nuevamente. (Evita ingresar un correo ya existente)");
            model.addAttribute("contactos", contactos);
            return "contactos";
        }

    }
    
    @GetMapping("/getEdit/{codigo}")
    public String editFormContactos(Model model,
            @PathVariable("codigo") Long id){
        Contactos contactos = servicioContactos.get(id);
        model.addAttribute("contactos", contactos);
        return "Contactos_edit";
    }
    
    @GetMapping("/delete")
    public String deleteFormColaborador(Model model,
            @RequestParam("id") Long id){
        servicioContactos.delete(id);
        return "redirect:/formResultadoContactos";
    }
    @GetMapping("/getListJSON")
    public ResponseEntity<List<Contactos>> listarContactosJSON (Model model){
            List<Contactos> lista = servicioContactos.getList();
            //model.addAttribute("lista", lista);
        return ResponseEntity.ok(lista);
}   
    @PostMapping("/updateContactos")
    public ResponseEntity<String> updateContactos(@RequestBody Contactos contactos) {
        try {
            servicioContactos.save(contactos); // Llama al servicio para actualizar el contacto
            return ResponseEntity.ok("Contacto actualizado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el contacto");
        }
    }


}

