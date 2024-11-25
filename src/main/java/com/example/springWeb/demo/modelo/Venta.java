/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.springWeb.demo.modelo;

import jakarta.persistence.*;
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

    @Column(name = "ticket", nullable = false, length = 50)
    private String ticket;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "precioTicket", nullable = false)
    private double precioTicket;

    @Column(name = "totalVenta", nullable = false)
    private double totalVenta;

    // Relación con Usuario (muchas ventas pertenecen a un usuario)
    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    // Relación con Compra (una venta está asociada a una compra)
    @ManyToOne
    @JoinColumn(name = "idCompra", nullable = false)
    private Compra compra;

    // Constructor por defecto
    public Venta() {
    }

    // Constructor con parámetros
    public Venta(LocalDate fechaVenta, String ticket, int cantidad, double precioTicket, Compra compra, Usuario usuario) {
        this.fechaVenta = fechaVenta;
        this.ticket = ticket;
        this.cantidad = cantidad;
        this.precioTicket = precioTicket;
        this.compra = compra;
        this.usuario = usuario;
        this.totalVenta = calcularTotalVenta();
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
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        }
        this.cantidad = cantidad;
        this.totalVenta = calcularTotalVenta(); // Actualiza el total al cambiar la cantidad
    }

    public double getPrecioTicket() {
        return precioTicket;
    }

    public void setPrecioTicket(double precioTicket) {
        if (precioTicket < 0) {
            throw new IllegalArgumentException("El precio del ticket no puede ser negativo.");
        }
        this.precioTicket = precioTicket;
        this.totalVenta = calcularTotalVenta(); // Actualiza el total al cambiar el precio
    }

    public double getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(double totalVenta) {
        this.totalVenta = totalVenta;
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

    // Lógica de cálculo
    private double calcularTotalVenta() {
        return this.cantidad * this.precioTicket;
    }

    // Método toString para depuración
    @Override
    public String toString() {
        return "Venta{" +
                "idVenta=" + idVenta +
                ", fechaVenta=" + fechaVenta +
                ", ticket='" + ticket + '\'' +
                ", cantidad=" + cantidad +
                ", precioTicket=" + precioTicket +
                ", totalVenta=" + totalVenta +
                '}';
    }
}
