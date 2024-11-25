/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.springWeb.demo.dao;

import com.example.springWeb.demo.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioDAO extends JpaRepository<Usuario, Integer> {

    // Método para iniciar sesión
    @Query("SELECT u FROM Usuario u WHERE u.email = :email AND u.contrasena = :contrasena")
    Usuario iniciarSesion(String email, String contrasena);

    // Consultas personalizadas adicionales (si es necesario)
    @Query("SELECT u FROM Usuario u WHERE u.email LIKE %:email%")
    List<Usuario> buscarUsuariosPorCorreo(String email);
    
     Usuario findByEmailAndContrasena(String email, String contrasena);
     
     boolean existsByEmail(String email);
}

