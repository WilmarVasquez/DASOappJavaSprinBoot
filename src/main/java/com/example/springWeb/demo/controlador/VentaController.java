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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaDAO ventaDAO;

    @Autowired
    private CompraDAO compraDAO;
    
    // Muestra el formulario de registro de compras
    @GetMapping("/regventa")
    public String mostrarFormularioVenta(HttpSession session, Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuarioLogueado == null) {
            return "redirect:/usuarios/login?error=Debes iniciar sesión primero";
        }

        model.addAttribute("venta", new Venta());
        return "venta"; // Archivo HTML para el formulario de registro
    }

    // Buscar Compra
    @GetMapping("/buscacompra")
    public String buscarCompra(@RequestParam("idCompra") int idCompra, HttpSession session, Model model) {
        Optional<Compra> compraOpt = compraDAO.findByIdCompra(idCompra);
        if (compraOpt.isPresent()) {
            Compra compra = compraOpt.get();
            session.setAttribute("idCompraSeleccionada", idCompra);
            model.addAttribute("compra", compra);
        } else {
            model.addAttribute("error", "Compra no encontrada");
        }
        return "venta";
    }

    // Registrar Venta
    @PostMapping("/registro")
    public String agregarVenta(
            @RequestParam String fechaVenta,
            @RequestParam String ticket,
            @RequestParam int cantidad,
            @RequestParam BigDecimal precioxTicket,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        Integer idCompra = (Integer) session.getAttribute("idCompraSeleccionada");

        if (usuario == null) {
            return "redirect:/usuarios/login?error=Debes iniciar sesión primero";
        }

        try {
            Compra compra = compraDAO.findByIdCompra(idCompra).orElse(null);
            if (compra == null) {
                model.addAttribute("error", "La compra asociada no existe.");
                return "venta";
            }

            BigDecimal totalInversion = precioxTicket.multiply(BigDecimal.valueOf(cantidad));

            Venta venta = new Venta();
            venta.setFechaVenta(LocalDate.parse(fechaVenta));
            venta.setTicket(ticket);
            venta.setCantidad(cantidad);
            venta.setPrecioxTicket(precioxTicket);
            venta.setTotalInversion(totalInversion);
            venta.setUsuario(usuario);
            venta.setCompra(compra);

            ventaDAO.save(venta);
            model.addAttribute("success", "Venta registrada correctamente");
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar venta: " + e.getMessage());
        }

        return "venta";
    }
}
