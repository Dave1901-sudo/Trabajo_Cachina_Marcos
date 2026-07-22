/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.configuraciones;

import com.example.SPA_CACHINA.entidades.Usuario;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author David
 */
public class SecurityUser implements UserDetails { // Adapta Usuario a UserDetails de Spring Security

    private Usuario user;

      public SecurityUser(Usuario user) { // Constructor que envuelve Usuario
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // Retorna rol del usuario como autoridad
        return Collections.singleton(new SimpleGrantedAuthority(user.getRole()));
    }

     @Override
    public String getPassword() { // Delega contraseña al usuario envuelto
        return user.getPassword();
    }

    @Override
    public String getUsername() { // Delega username al usuario envuelto
        return user.getUsername();
    }

}
