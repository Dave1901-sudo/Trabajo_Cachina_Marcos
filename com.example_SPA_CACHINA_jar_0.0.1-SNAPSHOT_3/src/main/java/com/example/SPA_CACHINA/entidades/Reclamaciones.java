/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.Modelo.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reclamaciones")
public class Reclamaciones {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idreclamaciones")
    private Long idreclamaciones;
    @Column(name="nombre_Completo")
    private String nombre_Completo;
    private String domicilio;
    @Column(name="dni_Ce")
    private String dni_Ce;
    private String telefono;
    @Column(name="correo_Electronico")
    private String correo_Electronico;
    @Column(name="tipo_Bien")
    private String tipo_Bien;
    @Column(name="monto_Reclamado")
    private double monto_Reclamado;
    @Column(name="descripcion_Bien")
    private String descripcion_Bien;
    @Column(name="tipo_Reclamacion")
    private String tipo_Reclamacion;
    @Column(name="detalle_Reclamacion")
    private String detalle_Reclamacion;
    @Column(name="detalle_Pedido")
    private String detalle_Pedido;

    /*public Reclamaciones() {
    }

    public Reclamaciones(String nombreCompleto, String domicilio, String dniCe, String telefono, String correoElectronico,
                              String tipoBien, double montoReclamado, String descripcionBien, String tipoReclamacion,
                              String detalleReclamacion, String detallePedido) {
        this.nombreCompleto = nombreCompleto;
        this.domicilio = domicilio;
        this.dniCe = dniCe;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.tipoBien = tipoBien;
        this.montoReclamado = montoReclamado;
        this.descripcionBien = descripcionBien;
        this.tipoReclamacion = tipoReclamacion;
        this.detalleReclamacion = detalleReclamacion;
        this.detallePedido = detallePedido;
    }

    // Getters and Setters
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getDniCe() {
        return dniCe;
    }

    public void setDniCe(String dniCe) {
        this.dniCe = dniCe;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getTipoBien() {
        return tipoBien;
    }

    public void setTipoBien(String tipoBien) {
        this.tipoBien = tipoBien;
    }

    public double getMontoReclamado() {
        return montoReclamado;
    }

    public void setMontoReclamado(double montoReclamado) {
        this.montoReclamado = montoReclamado;
    }

    public String getDescripcionBien() {
        return descripcionBien;
    }

    public void setDescripcionBien(String descripcionBien) {
        this.descripcionBien = descripcionBien;
    }

    public String getTipoReclamacion() {
        return tipoReclamacion;
    }

    public void setTipoReclamacion(String tipoReclamacion) {
        this.tipoReclamacion = tipoReclamacion;
    }

    public String getDetalleReclamacion() {
        return detalleReclamacion;
    }

    public void setDetalleReclamacion(String detalleReclamacion) {
        this.detalleReclamacion = detalleReclamacion;
    }

    public String getDetallePedido() {
        return detallePedido;
    }

    public void setDetallePedido(String detallePedido) {
        this.detallePedido = detallePedido;
    }*/
}
