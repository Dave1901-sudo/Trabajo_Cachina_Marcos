/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.controladores;

import com.example.SPA_CACHINA.entidades.Reservas;
import com.example.SPA_CACHINA.entidades.platos;
import com.example.SPA_CACHINA.locale.PedidoRequest;
import com.example.SPA_CACHINA.locale.ResponseMessage;
import com.example.SPA_CACHINA.servicios.PedidoService;
import com.example.SPA_CACHINA.servicios.Servicioplatos;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author David
 */
@Controller
public class ControladorPlatos {

    @Autowired
    Servicioplatos servicioplatos;

    @Autowired
    private PedidoService pedidoService;

    /* @GetMapping("/")
    public String listarPlatosVerticales(Model model) {
        List<platos> lista = servicioplatos.getList(); // Obtener la lista de platos
        model.addAttribute("lista", lista); // Agregar al modelo
         model.addAttribute("reservas", new Reservas());
        return "index"; // Retorna la vista index
    }*/
    @GetMapping("/")
    public String listarPlatosVerticales(Model model) {
        List<platos> lista = servicioplatos.getList(); // Obtener la lista de platos
        Map<String, List<platos>> platosPorCategoria = lista.stream()
                .collect(Collectors.groupingBy(platos::getCategoria)); // Agrupar por categoría
        model.addAttribute("platosPorCategoria", platosPorCategoria); // Agregar al modelo
        model.addAttribute("reservas", new Reservas());
        return "index"; // Retorna la vista index
    }

    /*@ModelAttribute("currentUser")
    public String getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName(); // Nombre del usuario autenticado
        }
        return null; // Si no está autenticado, no hay nombre
    }*/

    /*@ModelAttribute("isAdmin")
    public boolean isAdmin(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
        }
        return false;
    }*/

    @GetMapping("/formResultadoPlatos")
    public String listarPlatos(Model model) {
        List<platos> lista = servicioplatos.getList();
        model.addAttribute("lista", lista);
        return "formResultadoPlatos";
    }

    @GetMapping("/platos")
    public String formPlatos(Model model) {
        model.addAttribute("platos", new platos());
        return "platos";
    }

    @PostMapping("/registrarPlatos")
    public String grabarPlatos(
            @ModelAttribute platos platos,
            @RequestParam("imagenArchivo") MultipartFile imagenArchivo, // Recibe el archivo subido
            Model model) {
        try {
            // Convertir el archivo en un array de bytes
            if (!imagenArchivo.isEmpty()) {
                platos.setImagen(imagenArchivo.getBytes());
            } else {
                platos platoExistente = servicioplatos.get(platos.getIdplato());
                platos.setImagen(platoExistente.getImagen());
            }
            servicioplatos.save(platos);
            return "redirect:/formResultadoPlatos";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("platos", platos);
            return "platos";
        }
    }

    // Manejar la excepción MaxUploadSizeExceededException
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException exc, Model model) {
        model.addAttribute("errorMessage", "El archivo que intentas subir es demasiado grande.");
        return "platos"; // O la vista que prefieras mostrar
    }

    @GetMapping("/getEditPlatos/{codigos}")
    public String editFormPlatos(Model model,
            @PathVariable("codigos") Long id) {
        platos platos = servicioplatos.get(id);
        model.addAttribute("platos", platos);
        return "Platos_edit";
    }

    @GetMapping("/deletePlatos")
    public String deleteFormPlatos(Model model,
            @RequestParam("id") Long id) {
        servicioplatos.delete(id);
        return "redirect:/formResultadoPlatos";
    }

    @PostMapping("/realizarPedido")
    @ResponseBody
    public ResponseEntity<?> realizarPedido(@RequestBody PedidoRequest orderData) {
        try {
            pedidoService.guardarPedido(orderData);
            return ResponseEntity.ok().body(new ResponseMessage("success"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("error: " + e.getMessage()));
        }
    }

    @GetMapping("/imagen/{id}")
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable("id") Long id) {
        platos plato = servicioplatos.get(id);
        byte[] imagen = plato.getImagen();

        // Determinar el tipo de contenido según el formato de la imagen
        String contentType = identificarTipoDeImagen(imagen);

        if (contentType == null) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType)); // Establecer el tipo de contenido
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }

    private String identificarTipoDeImagen(byte[] imagen) {
        try {
            String contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imagen));
            return contentType;
        } catch (IOException e) {
            return null; // Si no se puede identificar el tipo de imagen
        }
    }

};
