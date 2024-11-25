/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.springWeb.demo.dao;

import com.example.springWeb.demo.modelo.Compra;
import com.example.springWeb.demo.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraDAO extends JpaRepository<Compra, Integer> {

    // Métodos adicionales personalizados (si es necesario)

    // Ejemplo: Buscar todas las compras de un usuario específico
    List<Compra> findByUsuarioIdUsuario(int idUsuario);

    // Ejemplo: Buscar compras por rango de fechas
    List<Compra> findByFechaCompraBetween(java.util.Date fechaInicio, java.util.Date fechaFin);
    
    List<Compra> findByUsuario(Usuario usuario);
}
