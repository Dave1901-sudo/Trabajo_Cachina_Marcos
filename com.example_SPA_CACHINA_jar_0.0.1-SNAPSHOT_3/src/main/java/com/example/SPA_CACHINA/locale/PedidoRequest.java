/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SPA_CACHINA.locale;

import java.util.List;

/**
 *
 * @author David
 */
public class PedidoRequest {
    private List<CarritoItem> orderItems;
    private String email;
    private String phone;
     private String direccion;

    public List<CarritoItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<CarritoItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    

    
}

