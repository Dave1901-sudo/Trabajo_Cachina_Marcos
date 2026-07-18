/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.SPA_CACHINA.repositorios;

import com.example.SPA_CACHINA.entidades.platos;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author David
 */
@Repository
public interface PlatosDAO 
            extends JpaRepository<platos, Long> {

    @Query("SELECT DISTINCT p.categoria FROM platos p ORDER BY p.categoria")
    List<String> findCategoriasDistinct();
}
