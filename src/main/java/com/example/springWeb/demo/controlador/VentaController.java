/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.springWeb.demo.controlador;

import com.example.springWeb.demo.dao.CompraDAO;
import com.example.springWeb.demo.dao.VentaDAO;
import com.example.springWeb.demo.modelo.Compra;
import com.example.springWeb.demo.modelo.Usuario;
import com.example.springWeb.demo.modelo.Venta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaDAO ventaDAO;

    @Autowired
    private CompraDAO compraDAO;

    // Muestra el formulario de registro de ventas
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/usuarios/login?error=Debes iniciar sesión primero";
        }

        List<Compra> compras = compraDAO.findByUsuario(usuario);
        model.addAttribute("compras", compras); // Mostrar las compras del usuario
        return "venta"; // Nombre de la plantilla Thymeleaf
    }

    // Maneja la acción de registrar una nueva venta
    @PostMapping("/registro")
    public String agregarVenta(@RequestParam int idCompra,
                                @RequestParam String ticket,
                                @RequestParam int cantidad,
                                @RequestParam double precioTicket,
                                HttpSession session,
                                Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/usuarios/login?error=Debes iniciar sesión primero";
        }

        try {
            // Buscar la compra asociada
            Compra compra = compraDAO.findById(idCompra).orElse(null);
            if (compra == null) {
                model.addAttribute("error", "La compra asociada no existe.");
                return "venta";
            }

            // Crear y configurar la venta
            Venta venta = new Venta();
            venta.setFechaVenta(LocalDate.now()); // Usar LocalDate para fechas
            venta.setTicket(ticket);
            venta.setCantidad(cantidad);
            venta.setPrecioTicket(precioTicket);
            venta.setTotalVenta(cantidad * precioTicket);
            venta.setUsuario(usuario);
            venta.setCompra(compra); // Asignar la compra

            // Guardar la venta
            ventaDAO.save(venta);
            model.addAttribute("success", "Venta registrada correctamente");
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar venta: " + e.getMessage());
        }

        return "venta";
    }

    // Maneja la acción de buscar una venta por su ID
    @GetMapping("/{id}")
    public String buscarVentaPorId(@PathVariable int id, Model model) {
        try {
            Venta venta = ventaDAO.findById(id).orElse(null);
            if (venta != null) {
                model.addAttribute("venta", venta);
                model.addAttribute("success", "Venta encontrada correctamente.");
            } else {
                model.addAttribute("error", "Venta no encontrada.");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al buscar venta: " + e.getMessage());
        }

        return "venta";
    }

    // Maneja la acción de actualizar una venta
    @PostMapping("/actualizar")
    public String actualizarVenta(@RequestParam int idVenta,
                                  @RequestParam String ticket,
                                  @RequestParam int cantidad,
                                  @RequestParam double precioTicket,
                                  @RequestParam int idCompra,
                                  HttpSession session,
                                  Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/usuarios/login?error=Debes iniciar sesión primero";
        }

        try {
            // Buscar la compra asociada
            Compra compra = compraDAO.findById(idCompra).orElse(null);
            if (compra == null) {
                model.addAttribute("error", "La compra asociada no existe.");
                return "venta";
            }

            // Buscar la venta existente
            Venta venta = ventaDAO.findById(idVenta).orElse(null);
            if (venta == null) {
                model.addAttribute("error", "La venta no existe.");
                return "venta";
            }

            // Actualizar los datos de la venta
            venta.setFechaVenta(LocalDate.now());
            venta.setTicket(ticket);
            venta.setCantidad(cantidad);
            venta.setPrecioTicket(precioTicket);
            venta.setTotalVenta(cantidad * precioTicket);
            venta.setCompra(compra); // Actualizar la compra asociada
            venta.setUsuario(usuario);

            ventaDAO.save(venta);
            model.addAttribute("success", "Venta actualizada correctamente");
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar venta: " + e.getMessage());
        }

        return "venta";
    }

    // Maneja la acción de eliminar una venta
    @PostMapping("/eliminar")
    public String eliminarVenta(@RequestParam int idVenta, Model model) {
        try {
            ventaDAO.deleteById(idVenta);
            model.addAttribute("success", "Venta eliminada correctamente");
        } catch (Exception e) {
            model.addAttribute("error", "Error al eliminar venta: " + e.getMessage());
        }

        return "venta";
    }

    // Listar todas las ventas
    @GetMapping
    public String listarVentas(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/usuarios/login?error=Debes iniciar sesión primero";
        }

        try {
            List<Venta> ventas = ventaDAO.findByUsuario(usuario);
            model.addAttribute("ventas", ventas);
        } catch (Exception e) {
            model.addAttribute("error", "Error al listar ventas: " + e.getMessage());
        }

        return "venta";
    }
}
