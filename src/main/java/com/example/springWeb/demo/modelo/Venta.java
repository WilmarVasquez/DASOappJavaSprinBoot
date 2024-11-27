/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.springWeb.demo.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "venta")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idVenta")
    private int idVenta;

    @Column(name = "fechaVenta", nullable = false)
    private LocalDate fechaVenta;

    @Column(name = "ticket", nullable = false, length = 100)
    private String ticket;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "precioxTicket", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioxTicket;

    @Column(name = "totalInversion", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalInversion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = true)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCompras", nullable = true)
    private Compra compra;

    // Constructor vacío
    public Venta() {
    }

    // Constructor con parámetros
    public Venta(LocalDate fechaVenta, String ticket, int cantidad, BigDecimal precioxTicket, BigDecimal totalInversion, Compra compra, Usuario usuario) {
        this.fechaVenta = fechaVenta;
        this.ticket = ticket;
        this.cantidad = cantidad;
        this.precioxTicket = precioxTicket;
        this.totalInversion = totalInversion;
        this.compra = compra;
        this.usuario = usuario;
    }

    // Getters y Setters
    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        actualizarTotalInversion();
    }

    public BigDecimal getPrecioxTicket() {
        return precioxTicket;
    }

    public void setPrecioxTicket(BigDecimal precioxTicket) {
        this.precioxTicket = precioxTicket;
        actualizarTotalInversion();
    }

    public BigDecimal getTotalInversion() {
        return totalInversion;
    }

    public void setTotalInversion(BigDecimal totalInversion) {
        this.totalInversion = totalInversion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    // Método para actualizar el total de la inversión
    private void actualizarTotalInversion() {
        if (precioxTicket != null && cantidad > 0) {
            this.totalInversion = precioxTicket.multiply(BigDecimal.valueOf(cantidad));
        }
    }

    @Override
    public String toString() {
        return "Venta{" +
                "idVenta=" + idVenta +
                ", fechaVenta=" + fechaVenta +
                ", ticket='" + ticket + '\'' +
                ", cantidad=" + cantidad +
                ", precioxTicket=" + precioxTicket +
                ", totalInversion=" + totalInversion +
                ", usuario=" + (usuario != null ? usuario.getIdUsuario() : "null") +
                ", compra=" + (compra != null ? compra.getIdCompra() : "null") +
                '}';
    }
}
