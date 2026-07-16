/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author David
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Userneg")
public class Usuario {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(unique = true)
    private String username;

    private String nombres;

    private String apellidos;

    @Column(unique = true)
    private String email;

    private String telefono;

    private String direccion;

    @Column(unique = true)
    private String documentoIdentidad;

    private String password;

    private String role;
}