package com.example.SPA_CACHINA.controladores.ApiPruebas;

import com.example.SPA_CACHINA.entidades.Resena;
import com.example.SPA_CACHINA.entidades.Usuario;
import com.example.SPA_CACHINA.servicios.ResenaService;
import com.example.SPA_CACHINA.servicios.UsuarioService;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resenas")
public class ApiResenasController {

    @Autowired
    private ResenaService resenaService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/{platoId}")
    public ResponseEntity<?> obtenerResenas(@PathVariable Long platoId) {
        List<Resena> resenas = resenaService.obtenerResenasAprobadas(platoId);
        return ResponseEntity.ok(resenas);
    }

    @PostMapping
    public ResponseEntity<?> crearResena(@RequestBody Map<String, Object> body, Principal principal) {
        try {
            Long platoId = Long.valueOf(body.get("platoId").toString());
            int puntuacion = Integer.parseInt(body.get("puntuacion").toString());
            String comentario = (String) body.get("comentario");

            String usuarioNombre = "Anónimo";
            Long usuarioId = null;
            if (principal != null) {
                Usuario usuario = usuarioService.getByUsername(principal.getName());
                if (usuario != null) {
                    usuarioId = usuario.getId();
                    usuarioNombre = usuario.getNombres() + " " + usuario.getApellidos();
                }
            }

            Resena resena = resenaService.crearResena(platoId, usuarioId, usuarioNombre, puntuacion, comentario);
            return ResponseEntity.ok(Map.of("mensaje", "Resena enviada para revision", "id", resena.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> darLike(@PathVariable Long id) {
        try {
            resenaService.darLike(id);
            return ResponseEntity.ok(Map.of("mensaje", "Like registrado"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/aprobar")
    public ResponseEntity<?> aprobarResena(@PathVariable Long id) {
        try {
            resenaService.aprobarResena(id);
            return ResponseEntity.ok(Map.of("mensaje", "Resena aprobada"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarResena(@PathVariable Long id) {
        try {
            resenaService.eliminarResena(id);
            return ResponseEntity.ok(Map.of("mensaje", "Resena eliminada"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/pendientes")
    public ResponseEntity<?> obtenerPendientes() {
        return ResponseEntity.ok(resenaService.obtenerResenasPendientes());
    }
}
