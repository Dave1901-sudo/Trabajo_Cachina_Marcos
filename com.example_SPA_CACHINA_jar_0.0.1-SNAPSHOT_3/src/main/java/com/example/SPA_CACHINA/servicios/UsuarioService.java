/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.servicios;

import com.example.SPA_CACHINA.entidades.Usuario;
import com.example.SPA_CACHINA.repositorios.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author David
 */
@Service
public class UsuarioService {
    // Chicana
       @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    
    public void createUser(String username, String rawPassword, String role) {
           if (userRepository.existsByUsername(username)) {
        throw new IllegalArgumentException("El nombre de usuario ya está en uso. Por favor, elija otro.");
    }

// Codificar la contraseña
        String encodedPassword = passwordEncoder.encode(rawPassword);
        
        // Crear el usuario
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(encodedPassword);
        usuario.setRole(role);
        
        // Guardar el usuario en la base de datos
        userRepository.save(usuario);
    }
    
    
    public void updateRole(String username, String newRole) {
        Usuario user = userRepository.findByUsername(username);
        if (user != null) {
            user.setRole(newRole);
            userRepository.save(user);
        } else {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }
    
    public void deleteUserByUsername(String username) {
    Usuario user = userRepository.findByUsername(username);
    if (user != null) {
        userRepository.delete(user); // Elimina el usuario de la base de datos
    } else {
        throw new UsernameNotFoundException("Usuario no encontrado");
    }
}


    public List<Usuario> getList() {
        return userRepository.findAll();
    }

    public Usuario save(Usuario usuario) {
        return userRepository.save(usuario);
    }
    public Usuario get(Long id){
        return userRepository.findById(id).orElse(null);
    }
    public void delete(Long id){
        userRepository.deleteById(id);
    }
}
