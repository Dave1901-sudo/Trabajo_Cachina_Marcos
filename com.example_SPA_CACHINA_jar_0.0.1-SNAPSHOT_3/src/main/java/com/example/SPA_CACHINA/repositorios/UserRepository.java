/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.SPA_CACHINA.repositorios;

import com.example.SPA_CACHINA.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author David
 */
@Repository
public interface UserRepository extends JpaRepository<Usuario, Long>{

    @Query("SELECT u FROM Usuario u WHERE " +
           "(:search IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.nombres) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.telefono) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:role IS NULL OR u.role = :role)")
    List<Usuario> buscarUsuarios(@Param("search") String search, @Param("role") String role);

    Usuario findByUsername(String username);
    
    Usuario findByEmail(String email);

    Usuario findByDocumentoIdentidad(String documentoIdentidad);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByDocumentoIdentidad(String documentoIdentidad);

}