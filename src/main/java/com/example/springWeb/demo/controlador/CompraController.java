/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.springWeb.demo.controlador;

import com.example.springWeb.demo.dao.CompraDAO;
import com.example.springWeb.demo.modelo.Compra;
import com.example.springWeb.demo.modelo.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@Controller
public class CompraController {

    @Autowired
    private CompraDAO compraDAO;

    // Muestra el formulario de registro de compras
    @GetMapping("/compras/registro")
    public String mostrarFormularioRegistro(HttpSession session, Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuarioLogueado == null) {
            return "redirect:/usuarios/login?error=Debes iniciar sesión primero";
        }

        model.addAttribute("compra", new Compra());
        return "compra"; // Archivo HTML para el formulario de registro
    }

    // Listar las compras realizadas
    @GetMapping("/compras")
    public String listarCompras(HttpSession session, Model model) {
      Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

      if (usuarioLogueado == null) {
          return "redirect:/usuarios/login?error=Debes iniciar sesión primero";
      }

      try {
          List<Compra> compras = compraDAO.findByUsuarioIdUsuario(usuarioLogueado.getIdUsuario());
          model.addAttribute("compras", compras);
      } catch (Exception e) {
          model.addAttribute("error", "Error al cargar las compras: " + e.getMessage());
          return "error"; // Una vista alternativa para mostrar errores
      }

      return "listarCompras"; // Archivo HTML para listar compras
  }


    // Maneja el registro de una nueva compra
    @PostMapping("/compras/agregar")
    public String agregarCompra(@RequestParam String ticket,
                                @RequestParam int cantidad,
                                @RequestParam double precioXticket,
                                HttpSession session,
                                Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuarioLogueado == null) {
            return "redirect:/usuarios/login?error=Debes iniciar sesión primero";
        }

        try {
            Compra nuevaCompra = new Compra();
            nuevaCompra.setfecha_compra(LocalDate.now());
            nuevaCompra.setTicket(ticket);
            nuevaCompra.setCantidad(cantidad);
            nuevaCompra.precioXticket(precioXticket);
            nuevaCompra.setTotalInversion(cantidad * precioXticket);
            nuevaCompra.setUsuario(usuarioLogueado);

            compraDAO.save(nuevaCompra);

            model.addAttribute("success", "Compra registrada exitosamente.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar compra: " + e.getMessage());
        }

        return "redirect:/compras";
    }

    // Maneja la eliminación de una compra
    @PostMapping("/compras/eliminar")
    public String eliminarCompra(@RequestParam int idCompra, Model model) {
        try {
            compraDAO.deleteById(idCompra);
            model.addAttribute("success", "Compra eliminada exitosamente.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al eliminar compra: " + e.getMessage());
        }

        return "redirect:/compras";
    }
}
