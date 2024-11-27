/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.springWeb.demo.dao;

import com.example.springWeb.demo.modelo.Compra;
import com.example.springWeb.demo.modelo.Venta;
import com.example.springWeb.demo.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaDAO extends JpaRepository<Venta, Integer> {

    // Consulta personalizada para listar ventas por usuario
    @Query("SELECT v FROM Venta v WHERE v.usuario.idUsuario = :idUsuario")
    List<Venta> listarVentasPorUsuario(int idUsuario);

    // Consulta personalizada para listar ventas asociadas a una compra
    @Query("SELECT v FROM Venta v WHERE v.compra.idCompra = :idCompra")
    List<Venta> listarVentasPorCompra(int idCompra);
    
    List<Venta> findByUsuario(Usuario usuario);
    
}

