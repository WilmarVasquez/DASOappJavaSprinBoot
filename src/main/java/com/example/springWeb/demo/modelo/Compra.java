/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.springWeb.demo.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compra")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCompra")
    private int idCompra;

    @Column(name = "fechaCompra", nullable = false)
    private LocalDate fechaCompra; // Reemplaza TemporalType.DATE con LocalDate

    @Column(name = "ticket", nullable = false, length = 50)
    private String ticket;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "precioXticket", nullable = false)
    private double precioXticket;

    @Column(name = "totalInversion", nullable = false)
    private double totalInversion;

    // Relación con Usuario (muchas compras pertenecen a un usuario)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    // Relación con Venta (una compra puede tener muchas ventas)
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Venta> ventas = new ArrayList<>();

    // Constructor por defecto
    public Compra() {
    }

    // Constructor con parámetros
    public Compra(LocalDate fechaCompra, String ticket, int cantidad, double precioXticket, Usuario usuario) {
        this.fechaCompra = fechaCompra;
        this.ticket = ticket;
        this.cantidad = cantidad;
        this.precioXticket = precioXticket;
        this.totalInversion = calcularTotalInversion();
        this.usuario = usuario;
    }

    // Getters y Setters
    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public LocalDate getfechaCompra() {
        return fechaCompra;
    }

    public void setfechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
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
        this.totalInversion = calcularTotalInversion(); // Actualiza el total
    }

    public double getprecioXticket() {
        return precioXticket;
    }

    public void precioXticket(double precioXticket) {
        this.precioXticket = precioXticket;
        this.totalInversion = calcularTotalInversion(); // Actualiza el total
    }

    public double getTotalInversion() {
        return totalInversion;
    }

    public void setTotalInversion(double totalInversion) {
        this.totalInversion = totalInversion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(List<Venta> ventas) {
        this.ventas = ventas != null ? ventas : new ArrayList<>();
    }

    // Métodos de relación
    public void agregarVenta(Venta venta) {
        this.ventas.add(venta);
        venta.setCompra(this); // Vincula la venta con esta compra
    }

    public void removerVenta(Venta venta) {
        this.ventas.remove(venta);
        venta.setCompra(null); // Desvincula la venta
    }

    // Lógica de cálculo
    private double calcularTotalInversion() {
        return this.cantidad * this.precioXticket;
    }

    // Método toString para depuración
    @Override
    public String toString() {
        return "Compra{" +
                "idCompra=" + idCompra +
                ", fechaCompra=" + fechaCompra +
                ", ticket='" + ticket + '\'' +
                ", cantidad=" + cantidad +
                ", precioXticket=" + precioXticket +
                ", totalInversion=" + totalInversion +
                '}';
    }
}
