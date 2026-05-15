/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.controladores;

import com.example.SPA_CACHINA.entidades.Usuario;
import com.example.SPA_CACHINA.servicios.UsuarioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author David
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/roleForm")
    public String showRoleUpdateForm() {
        return "updateRole"; // Nombre del archivo Thymeleaf: updateRole.html
    }

    @PostMapping("/updateRole")
    public String updateRole(@RequestParam String username, @RequestParam String role) {
        usuarioService.updateRole(username, role);
        return "redirect:/admin"; // Redirigir a una página de administración
    }
    
     @GetMapping("/printTemplate")
    public String showPrintTemplate(Model model) {
        // Agregar lógica para pasar datos al modelo si es necesario
        return "printTemplate";
    }

    @GetMapping("/userList")
    public String showUserList(Model model) {
        List<Usuario> users = usuarioService.getList();
        model.addAttribute("users", users); // Pasar la lista de usuarios al modelo
        return "userList"; // Nombre del archivo Thymeleaf para la lista de usuarios
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam String username) {
        try {
            usuarioService.deleteUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            // Puedes agregar una forma de manejar el error si el usuario no existe
            return "redirect:/admin/userList?error=userNotFound"; // Redirige con un mensaje de error
        }
        return "redirect:/admin/userList"; // Redirige a la lista de usuarios después de eliminar
    }

}
