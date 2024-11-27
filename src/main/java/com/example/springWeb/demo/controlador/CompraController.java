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
import java.util.Optional;

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

    // Buscar Compra
    @GetMapping("/compras/comprabusca")
    public String buscarCompra(@RequestParam("idCompra") int idCompra, HttpSession session, Model model) {
        Optional<Compra> compraOpt = compraDAO.findByIdCompra(idCompra);
        if (compraOpt.isPresent()) {
            Compra compra = compraOpt.get();
            session.setAttribute("idCompraSeleccionada", idCompra);
            model.addAttribute("compra", compra);
        } else {
            model.addAttribute("error", "Compra no encontrada");
        }
        return "compra";
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
            nuevaCompra.setfechaCompra(LocalDate.now());
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

        return "redirect:/dashboard";
    }

            // Método para borrar una compra por su ID
        @PostMapping("/compras/borrar")
        public String borrarCompra(@RequestParam("idCompra") int idCompra, Model model) {
            try {
                // Verificar si la compra existe
                if (compraDAO.existsById(idCompra)) {
                    // Eliminar la compra
                    compraDAO.deleteById(idCompra);
                    model.addAttribute("success", "Compra eliminada correctamente.");
                } else {
                    model.addAttribute("error", "La compra con el ID especificado no existe.");
                }
            } catch (Exception e) {
                model.addAttribute("error", "Error al borrar la compra: " + e.getMessage());
            }
            return "compra"; // Retornar a la misma vista después de la operación
        }

}
