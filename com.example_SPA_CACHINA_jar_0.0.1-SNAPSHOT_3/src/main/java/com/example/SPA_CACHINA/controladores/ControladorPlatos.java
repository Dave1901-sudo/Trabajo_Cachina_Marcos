/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.controladores;

import com.example.SPA_CACHINA.entidades.Reservas;
import com.example.SPA_CACHINA.entidades.Pedido;
import com.example.SPA_CACHINA.entidades.PedidoDetalle;
import com.example.SPA_CACHINA.entidades.Usuario;
import com.example.SPA_CACHINA.entidades.platos;
import com.example.SPA_CACHINA.locale.PedidoRequest;
import com.example.SPA_CACHINA.locale.ResponseMessage;
import com.example.SPA_CACHINA.servicios.PedidoService;
import com.example.SPA_CACHINA.servicios.Servicioplatos;
import com.example.SPA_CACHINA.servicios.UsuarioService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author David
 */
@Controller
public class ControladorPlatos { // Controlador principal de platos, carrito y pedidos

    @Autowired
    Servicioplatos servicioplatos;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioService usuarioService;

    /* @GetMapping("/")
    public String listarPlatosVerticales(Model model) {
        List<platos> lista = servicioplatos.getList(); // Obtener la lista de platos
        model.addAttribute("lista", lista); // Agregar al modelo
         model.addAttribute("reservas", new Reservas());
        return "index"; // Retorna la vista index
    }*/
    @GetMapping("/")
    public String listarPlatosVerticales(Model model) { // Muestra inicio con platos habilitados agrupados por categoría
        List<platos> lista = servicioplatos.getList().stream()
                .filter(platos::isHabilitado)
                .collect(Collectors.toList());
        Map<String, List<platos>> platosPorCategoria = lista.stream()
                .collect(Collectors.groupingBy(platos::getCategoria));
        model.addAttribute("platosPorCategoria", platosPorCategoria);
        model.addAttribute("reservas", new Reservas());
        return "index";
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
    public String listarPlatos(Model model) { // Muestra todos los platos para admin
        List<platos> lista = servicioplatos.getList();
        model.addAttribute("lista", lista);
        return "formResultadoPlatos";
    }

    @GetMapping("/platos")
    public String formPlatos(Model model) { // Muestra formulario de creación de plato
        model.addAttribute("platos", new platos());
        model.addAttribute("categorias", servicioplatos.getCategoriasDistinct());
        return "platos";
    }

    @PostMapping("/registrarPlatos")
    public String grabarPlatos( // Guarda o actualiza un plato con imagen
            @ModelAttribute platos platos,
            @RequestParam("imagenArchivo") MultipartFile imagenArchivo,
            Model model) {
        try {
            if (!imagenArchivo.isEmpty()) {
                platos.setImagen(imagenArchivo.getBytes());
            } else {
                platos platoExistente = servicioplatos.get(platos.getIdplato());
                if (platoExistente != null) {
                    platos.setImagen(platoExistente.getImagen());
                }
            }
            servicioplatos.save(platos);
            return "redirect:/formResultadoPlatos";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "El archivo que intentas subir es demasiado grande.");
            model.addAttribute("platos", platos);
            model.addAttribute("categorias", servicioplatos.getCategoriasDistinct());
            String view = (platos.getIdplato() != null) ? "Platos_edit" : "platos";
            return view;
        }
    }

    @GetMapping("/getEditPlatos/{codigos}")
    public String editFormPlatos(Model model, // Muestra formulario de edición de plato
            @PathVariable("codigos") Long id) {
        platos platos = servicioplatos.get(id);
        model.addAttribute("platos", platos);
        model.addAttribute("categorias", servicioplatos.getCategoriasDistinct());
        return "Platos_edit";
    }

    @GetMapping("/deletePlatos")
    public String deleteFormPlatos(Model model, // Elimina un plato
            @RequestParam("id") Long id) {
        servicioplatos.delete(id);
        return "redirect:/formResultadoPlatos";
    }

    @GetMapping("/toggleHabilitado")
    public String toggleHabilitado(@RequestParam("id") Long id) { // Alterna habilitación del plato
        servicioplatos.toggleHabilitado(id);
        return "redirect:/formResultadoPlatos";
    }

    @PostMapping("/realizarPedido")
    @ResponseBody
    public ResponseEntity<?> realizarPedido(@RequestBody PedidoRequest orderData, Principal principal) { // Procesa envío de pedido
        try {
            Usuario usuario = obtenerUsuarioLogueado(principal);
            pedidoService.guardarPedido(orderData, usuario);
            return ResponseEntity.ok().body(new ResponseMessage("success"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("error: " + e.getMessage()));
        }
    }

    @GetMapping("/mis-pedidos/pendientes")
    @ResponseBody
    public ResponseEntity<?> listarPedidosPendientes(Principal principal) { // Lista pedidos pendientes del usuario
        Usuario usuario = obtenerUsuarioLogueado(principal);
        if (usuario == null || usuario.getId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Map<String, Object>> pedidos = pedidoService
                .obtenerPedidosPendientesPorUsuario(usuario.getId())
                .stream()
                .map(this::resumenPedido)
                .collect(Collectors.toList());

        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/mis-pedidos/confirmados")
    @ResponseBody
    public ResponseEntity<?> listarPedidosConfirmados(Principal principal) { // Lista pedidos confirmados del usuario
        Usuario usuario = obtenerUsuarioLogueado(principal);
        if (usuario == null || usuario.getId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Map<String, Object>> pedidos = pedidoService
                .obtenerPedidosConfirmadosPorUsuario(usuario.getId())
                .stream()
                .map(this::resumenPedido)
                .collect(Collectors.toList());

        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/mis-pedidos/{id}")
    @ResponseBody
    public ResponseEntity<?> obtenerMiPedido(@PathVariable("id") Long id, Principal principal) { // Obtiene detalle de pedido por ID
        Usuario usuario = obtenerUsuarioLogueado(principal);
        if (usuario == null || usuario.getId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Pedido pedido = pedidoService.obtenerPedidoPendientePorIdYUsuario(id, usuario.getId());
            return ResponseEntity.ok(detallePedido(pedido));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Pedido no encontrado"));
        }
    }

    @PostMapping("/mis-pedidos/cancelar/{id}")
    @ResponseBody
    public ResponseEntity<?> cancelarPedido(@PathVariable("id") Long id, Principal principal) { // Cancela un pedido pendiente
        Usuario usuario = obtenerUsuarioLogueado(principal);
        if (usuario == null || usuario.getId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage("Debes iniciar sesión"));
        }
        try {
            pedidoService.cancelarPedido(id, usuario.getId());
            return ResponseEntity.ok(new ResponseMessage("Pedido cancelado exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
        }
    }

    private Usuario obtenerUsuarioLogueado(Principal principal) { // Obtiene usuario autenticado desde principal
        if (principal == null) {
            return null;
        }
        return usuarioService.getByUsername(principal.getName());
    }

    private Map<String, Object> resumenPedido(Pedido pedido) { // Construye mapa resumen del pedido
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", pedido.getId());
        data.put("fecha", formatearFecha(pedido.getFechaPedido()));
        data.put("fechaRaw", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(pedido.getFechaPedido()));
        data.put("total", pedido.getTotal());
        data.put("estado", pedido.getEstado());
        return data;
    }

    private Map<String, Object> detallePedido(Pedido pedido) { // Construye mapa detallado del pedido
        Map<String, Object> data = resumenPedido(pedido);
        data.put("email", pedido.getEmail());
        data.put("phone", pedido.getPhone());
        data.put("direccion", pedido.getDireccion());

        List<Map<String, Object>> detalles = new ArrayList<>();
        if (pedido.getDetalles() != null) {
            for (PedidoDetalle detalle : pedido.getDetalles()) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("nombre", detalle.getNombre());
                item.put("cantidad", detalle.getCantidad());
                item.put("precio", detalle.getPrecio());
                item.put("subtotal", detalle.getPrecio() * detalle.getCantidad());
                item.put("comentario", detalle.getComentario());
                detalles.add(item);
            }
        }
        data.put("detalles", detalles);
        return data;
    }

    private String formatearFecha(java.util.Date fecha) { // Formatea fecha para mostrar
        if (fecha == null) {
            return "";
        }
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(fecha);
    }

    @GetMapping("/imagen/{id}")
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable("id") Long id) { // Sirve imagen del plato
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

    private String identificarTipoDeImagen(byte[] imagen) { // Adivina content-type desde bytes de imagen
        try {
            String contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imagen));
            return contentType;
        } catch (IOException e) {
            return null; // Si no se puede identificar el tipo de imagen
        }
    }

};
