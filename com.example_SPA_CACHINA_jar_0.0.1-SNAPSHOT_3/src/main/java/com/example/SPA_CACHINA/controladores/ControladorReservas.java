/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.controladores;

import com.example.SPA_CACHINA.entidades.Reservas;
import com.example.SPA_CACHINA.servicios.ServicioReservas;
import com.example.SPA_CACHINA.servicios.UsuarioService;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author David
 */
@Controller
public class ControladorReservas { // Controlador de reservas

    @Autowired
    ServicioReservas servicioReservas;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/formResultadoReservas")
    public String listarReservas(Model model) { // Muestra todas las reservas
        List<Reservas> lista = servicioReservas.getList();
        model.addAttribute("lista", lista);
        return "formResultadoReservas";
    }

    @PostMapping("/registrarReservas")
    public String grabarContactos( // Guarda nueva reserva con validación
            @ModelAttribute Reservas reservas, Principal principal, RedirectAttributes redirectAttributes) {
        if (principal != null) {
            reservas.setNombre(usuarioService.getByUsername(principal.getName()).getNombres()
                    + " " + usuarioService.getByUsername(principal.getName()).getApellidos());
            reservas.setCorreo(usuarioService.getByUsername(principal.getName()).getEmail());
        }
        String horaOriginal = reservas.getHora();
        String fechaStr = reservas.getFecha();
        if (fechaStr != null) {
            ZoneId peru = ZoneId.of("America/Lima");
            LocalDate fechaReserva = LocalDate.parse(fechaStr);
            if (fechaReserva.isBefore(LocalDate.now(peru))) {
                redirectAttributes.addFlashAttribute("errorMessage", "No puedes reservar en una fecha pasada.");
                return "redirect:/#reservas";
            }
            if (fechaReserva.equals(LocalDate.now(peru)) && horaOriginal != null) {
                if (!LocalTime.parse(horaOriginal).isAfter(LocalTime.now(peru))) {
                    redirectAttributes.addFlashAttribute("errorMessage", "No puedes reservar en una hora pasada.");
                    return "redirect:/#reservas";
                }
            }
        }
        long count = servicioReservas.contarPorFechaYHora(fechaStr, horaOriginal);
        if (count >= 10) {
            redirectAttributes.addFlashAttribute("errorMessage", "No hay mesas disponibles para esa fecha y hora.");
            return "redirect:/#reservas";
        }
        reservas.setCreatedAt(LocalDateTime.now(ZoneId.of("America/Lima")));
        servicioReservas.save(reservas);
        redirectAttributes.addFlashAttribute("reservaCreada", true);
        return "redirect:/#reservas";
    }

    @GetMapping("/getEditReservas/{codigoReservas}")
    public String editFormContactos(Model model, // Muestra formulario de edición de reserva
            @PathVariable("codigoReservas") Long id){
        Reservas reservas = servicioReservas.get(id);
        model.addAttribute("reservas", reservas);
        return "Reservas_edit";
    }

    @GetMapping("/deleteReservas")
    public String deleteFormColaborador(Model model, // Elimina una reserva
            @RequestParam("id") Long id){
        servicioReservas.delete(id);
        return "redirect:/formResultadoReservas";
    }

    @GetMapping("/disponibilidad")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> disponibilidad( // Verifica disponibilidad de mesas
            @RequestParam String fecha, @RequestParam String hora) {
        long count = servicioReservas.contarPorFechaYHora(fecha, hora);
        long disponibles = 10 - count;
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("disponibles", Math.max(disponibles, 0));
        res.put("ocupadas", count);
        res.put("total", 10);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/mis-reservas")
    @ResponseBody
    public ResponseEntity<List<Reservas>> misReservas(Principal principal) { // Obtiene reservas del usuario
        if (principal == null) return ResponseEntity.ok(List.of());
        String email = usuarioService.getByUsername(principal.getName()).getEmail();
        return ResponseEntity.ok(servicioReservas.obtenerPorCorreo(email));
    }

    @PostMapping("/cancelarMiReserva")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> cancelarMiReserva( // Cancela reserva propia dentro de 5 min
            @RequestParam Long id, Principal principal) {
        Map<String, Object> res = new LinkedHashMap<>();
        if (principal == null) {
            res.put("success", false);
            res.put("message", "Debes iniciar sesión.");
            return ResponseEntity.status(401).body(res);
        }
        Reservas reserva = servicioReservas.get(id);
        if (reserva == null) {
            res.put("success", false);
            res.put("message", "Reserva no encontrada.");
            return ResponseEntity.status(404).body(res);
        }
        String email = usuarioService.getByUsername(principal.getName()).getEmail();
        if (!reserva.getCorreo().equals(email)) {
            res.put("success", false);
            res.put("message", "No puedes cancelar una reserva que no es tuya.");
            return ResponseEntity.status(403).body(res);
        }
        if (reserva.getCreatedAt() != null) {
            LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Lima"));
            if (reserva.getCreatedAt().plusMinutes(5).isBefore(ahora)) {
                res.put("success", false);
                res.put("message", "Ya pasaron más de 5 minutos, no puedes cancelar esta reserva.");
                return ResponseEntity.ok(res);
            }
        }
        servicioReservas.delete(id);
        res.put("success", true);
        res.put("message", "Reserva cancelada exitosamente.");
        return ResponseEntity.ok(res);
    }
}
