/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.springWeb.demo.controlador;

import com.example.springWeb.demo.modelo.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador para manejar las operaciones del Dashboard.
 * Asegura que solo los usuarios autenticados puedan acceder.
 *
 * @author alberto
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    /**
     * Muestra la página del Dashboard.
     *
     * @param session La sesión HTTP actual.
     * @param model   Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el dashboard.
     */
   @GetMapping
    public String mostrarDashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            System.out.println("Usuario no autenticado. Redirigiendo a login.");
            return "redirect:/usuarios/login?error=Debes iniciar sesión primero";
        }
        model.addAttribute("usuarioLogueado", usuario);
        return "dashboard";
    }
    
   
}

