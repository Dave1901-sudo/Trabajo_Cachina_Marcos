/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.controladores;

import com.example.SPA_CACHINA.entidades.Auditoria;
import com.example.SPA_CACHINA.entidades.Resena;
import com.example.SPA_CACHINA.entidades.Usuario;
import com.example.SPA_CACHINA.entidades.platos;
import com.example.SPA_CACHINA.servicios.AuditoriaService;
import com.example.SPA_CACHINA.servicios.ResenaService;
import com.example.SPA_CACHINA.servicios.Servicioplatos;
import com.example.SPA_CACHINA.servicios.UsuarioService;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author David
 */
@Controller
@RequestMapping("/admin")
public class AdminController { // Controlador del panel de administración

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ResenaService resenaService;

    @Autowired
    private AuditoriaService auditoriaService;

    @Autowired
    private Servicioplatos servicioplatos;

    @GetMapping("/roleForm")
    public String showRoleUpdateForm(Model model) { // Muestra formulario de actualización de rol

        model.addAttribute("users", usuarioService.getList());

        return "updateRole";
    }
    
    @GetMapping("/getUser")
    @ResponseBody
    public Usuario getUser(@RequestParam String username) { // Obtiene datos del usuario como JSON

        return usuarioService.getByUsername(username);

    }

    @PostMapping("/updateRole")
    public String updateRole( // Actualiza rol/credenciales del usuario
            @RequestParam String username,
            @RequestParam(required = false) String nuevoUsername,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String confirmPassword,
            @RequestParam String role,
            Model model,
            Principal principal) {

        try {

            usuarioService.updateCredentials(
                    username,
                    nuevoUsername,
                    password,
                    confirmPassword,
                    role
            );

            String admin = (principal != null) ? principal.getName() : "desconocido";
            auditoriaService.registrar(admin, "Actualizar rol", "Usuario: " + username + " -> Rol: " + role);

            return "redirect:/admin";

        } catch (IllegalArgumentException e) {

            model.addAttribute("errorMessage", e.getMessage());

            model.addAttribute("users", usuarioService.getList());

            return "updateRole";
        }
    }
    
     @GetMapping("/resenas")
    public String gestionarResenas( // Gestiona reseñas con filtros
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Long platoId,
            @RequestParam(required = false) String search,
            Model model) {
        List<Resena> resenas = resenaService.buscarResenas(estado, platoId, search);

        Map<Long, String> platoNombres = new HashMap<>();
        List<platos> todosPlatos = servicioplatos.getList();
        for (platos p : todosPlatos) {
            platoNombres.put(p.getIdplato(), p.getNombre());
        }

        model.addAttribute("resenas", resenas);
        model.addAttribute("platoNombres", platoNombres);
        model.addAttribute("todosPlatos", todosPlatos);
        model.addAttribute("estado", estado);
        model.addAttribute("platoId", platoId);
        model.addAttribute("search", search);
        return "adminResenas";
    }

    @GetMapping("/resenas/aprobar/{id}")
    public String aprobarResena(@PathVariable Long id, Principal principal) { // Aprueba una reseña
        resenaService.aprobarResena(id);
        String admin = (principal != null) ? principal.getName() : "desconocido";
        auditoriaService.registrar(admin, "Aprobar reseña", "Reseña ID: " + id);
        return "redirect:/admin/resenas";
    }

    @GetMapping("/resenas/eliminar/{id}")
    public String eliminarResena(@PathVariable Long id, Principal principal) { // Elimina una reseña
        resenaService.eliminarResena(id);
        String admin = (principal != null) ? principal.getName() : "desconocido";
        auditoriaService.registrar(admin, "Eliminar reseña", "Reseña ID: " + id);
        return "redirect:/admin/resenas";
    }

    @GetMapping("/printTemplate")
    public String showPrintTemplate(Model model) { // Muestra plantilla de impresión
        // Agregar lógica para pasar datos al modelo si es necesario
        return "printTemplate";
    }

    @GetMapping("/userList")
    public String showUserList( // Muestra lista de usuarios con búsqueda/filtro
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            Model model) {
        List<Usuario> users = usuarioService.buscarUsuarios(search, role);
        model.addAttribute("users", users);
        model.addAttribute("search", search);
        model.addAttribute("role", role);
        return "userList";
    }
    
    @GetMapping("/editUser")
    public String showEditUser(@RequestParam Long id, Model model) { // Muestra formulario de edición de usuario

        Usuario usuario = usuarioService.get(id);

        if (usuario == null) {
            return "redirect:/admin/userList";
        }

        model.addAttribute("usuario", usuario);

        return "editUser";
    }
    
    @PostMapping("/updateUser")
    public String updateUser( // Actualiza datos del perfil del usuario
            @RequestParam Long id,
            @RequestParam(required = false) String newUsername,
            @RequestParam String nombres,
            @RequestParam String apellidos,
            @RequestParam String email,
            @RequestParam String telefono,
            @RequestParam String direccion,
            @RequestParam String documentoIdentidad,
            Model model) {

        try {

            usuarioService.updateUser(
                    id,
                    newUsername,
                    nombres,
                    apellidos,
                    email,
                    telefono,
                    direccion,
                    documentoIdentidad
            );

            return "redirect:/admin/userList";

        } catch (IllegalArgumentException e) {

            Usuario usuario = usuarioService.get(id);

            // Sobrescribir únicamente los datos que el administrador escribió
            usuario.setNombres(nombres);
            usuario.setApellidos(apellidos);
            usuario.setEmail(email);
            usuario.setTelefono(telefono);
            usuario.setDireccion(direccion);
            usuario.setDocumentoIdentidad(documentoIdentidad);

            model.addAttribute("usuario", usuario);
            model.addAttribute("errorMessage", e.getMessage());

            return "editUser";
        }
    }

    @GetMapping("/confirmarEliminarUsuario")
    public String confirmarEliminarUsuario(@RequestParam String username, Model model) { // Muestra confirmación de eliminación
        model.addAttribute("username", username);
        return "confirmarEliminarUsuario";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam String username, Principal principal) { // Elimina cuenta de usuario
        try {
            usuarioService.deleteUserByUsername(username);
            String admin = (principal != null) ? principal.getName() : "desconocido";
            auditoriaService.registrar(admin, "Eliminar usuario", "Usuario eliminado: " + username);
        } catch (UsernameNotFoundException e) {
            return "redirect:/admin/userList?error=userNotFound";
        }
        return "redirect:/admin/userList";
    }

}
