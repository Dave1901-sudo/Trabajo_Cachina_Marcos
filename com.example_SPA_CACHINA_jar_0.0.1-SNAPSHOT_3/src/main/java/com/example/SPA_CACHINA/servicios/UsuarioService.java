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
    
    
    public void createUser(
            String username,
            String nombres,
            String apellidos,
            String email,
            String telefono,
            String direccion,
            String documentoIdentidad,
            String rawPassword,
            String role) {

        // Validación de unicidad
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }
        if (userRepository.existsByDocumentoIdentidad(documentoIdentidad)) {
            throw new IllegalArgumentException("El documento de identidad ya está registrado.");
        }

        // Codificar la contraseña
        String encodedPassword = passwordEncoder.encode(rawPassword);
        
        // Crear el usuario
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setNombres(nombres);
        usuario.setApellidos(apellidos);
        usuario.setEmail(email);
        usuario.setTelefono(telefono);
        usuario.setDireccion(direccion);
        usuario.setDocumentoIdentidad(documentoIdentidad);
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
    
    public void updateCredentials(
            String username,
            String nuevoUsername,
            String password,
            String confirmPassword,
            String role) {

        Usuario usuario = userRepository.findByUsername(username);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado.");
        }

        // Cambiar username (solo si se escribió uno nuevo)
        if (nuevoUsername != null && !nuevoUsername.trim().isEmpty()) {

            Usuario existente = userRepository.findByUsername(nuevoUsername);

            if (existente != null && !existente.getId().equals(usuario.getId())) {
                throw new IllegalArgumentException("Ese nombre de usuario ya existe.");
            }

            usuario.setUsername(nuevoUsername);
        }

        // Cambiar contraseña (solo si se escribió una)
        if (password != null && !password.trim().isEmpty()) {

            if (!password.equals(confirmPassword)) {
                throw new IllegalArgumentException("Las contraseñas no coinciden.");
            }

            usuario.setPassword(passwordEncoder.encode(password));
        }

        // Actualizar rol
        usuario.setRole(role);

        userRepository.save(usuario);
    }
    
    public void deleteUserByUsername(String username) {
        Usuario user = userRepository.findByUsername(username);
        if (user != null) {
            userRepository.delete(user); // Elimina el usuario de la base de datos
        } else {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }
    
    public void updateUser(
            Long id,
            String newUsername,
            String nombres,
            String apellidos,
            String email,
            String telefono,
            String direccion,
            String documentoIdentidad) {

            Usuario usuario = userRepository.findById(id)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            if (newUsername != null && !newUsername.trim().isEmpty()) {
                Usuario existente = userRepository.findByUsername(newUsername.trim());
                if (existente != null && !existente.getId().equals(id)) {
                    throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
                }
                usuario.setUsername(newUsername.trim());
            }

            // Validar correo
            Usuario usuarioCorreo = userRepository.findByEmail(email);
            if (usuarioCorreo != null && !usuarioCorreo.getId().equals(id)) {
                throw new IllegalArgumentException("El correo electrónico ya está registrado.");
            }

            // Validar documento
            Usuario usuarioDocumento = userRepository.findByDocumentoIdentidad(documentoIdentidad);
            if (usuarioDocumento != null && !usuarioDocumento.getId().equals(id)) {
                throw new IllegalArgumentException("El documento de identidad ya está registrado.");
            }

            usuario.setNombres(nombres);
            usuario.setApellidos(apellidos);
            usuario.setEmail(email);
            usuario.setTelefono(telefono);
            usuario.setDireccion(direccion);
            usuario.setDocumentoIdentidad(documentoIdentidad);

            userRepository.save(usuario);
    }
    
    public void actualizarCampoPerfil(
            String usernameLogueado,
            Long id,
            String campo,
            String valor) {

        Usuario usuario = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado."));

        // Seguridad: solo puede modificar su propio perfil
        if (!usuario.getUsername().equals(usernameLogueado)) {
            throw new IllegalArgumentException("No autorizado para modificar este perfil.");
        }

        valor = valor.trim();

        switch (campo) {

            case "nombres":
                usuario.setNombres(valor);
                break;

            case "apellidos":
                usuario.setApellidos(valor);
                break;

            case "telefono":
                usuario.setTelefono(valor);
                break;

            case "direccion":
                usuario.setDireccion(valor);
                break;

            case "email":

                Usuario usuarioCorreo = userRepository.findByEmail(valor);

                if (usuarioCorreo != null
                        && !usuarioCorreo.getId().equals(usuario.getId())) {

                    throw new IllegalArgumentException("El correo electrónico ya está registrado.");
                }

                usuario.setEmail(valor);
                break;

            default:
                throw new IllegalArgumentException("Campo no permitido.");
        }

        userRepository.save(usuario);
    }
    
    public List<Usuario> getList() {
        return userRepository.findAll();
    }

    public List<Usuario> buscarUsuarios(String search, String role) {
        if ("todos".equals(role)) role = null;
        if (search != null && search.trim().isEmpty()) search = null;
        return userRepository.buscarUsuarios(search, role);
    }

    public Usuario save(Usuario usuario) {
        return userRepository.save(usuario);
    }
    public Usuario get(Long id){
        return userRepository.findById(id).orElse(null);
    }
    
    public Usuario getByUsername(String username){
        return userRepository.findByUsername(username);
    }
    
    public void delete(Long id){
        userRepository.deleteById(id);
    }
}