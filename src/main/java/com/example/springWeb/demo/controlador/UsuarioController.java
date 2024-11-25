/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.springWeb.demo.controlador;

import com.example.springWeb.demo.dao.UsuarioDAO;
import com.example.springWeb.demo.modelo.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioDAO usuarioDAO;

    // Redirección desde la raíz al login
    @GetMapping("/")
    public String redireccionarLogin() {
        return "/usuarios/login";
    }

    // Muestra la página de login
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login"; // Nombre de la plantilla de login (login.html)
    }

    // Maneja la acción de inicio de sesión
    @PostMapping("/login")
    public String iniciarSesion(@RequestParam String email,
                                @RequestParam String contrasena,
                                HttpSession session,
                                Model model) {
        try {
            Usuario usuario = usuarioDAO.findByEmailAndContrasena(email, contrasena);

            if (usuario != null) {
                session.setAttribute("usuarioLogueado", usuario);
                return "redirect:/dashboard"; // Redirige al dashboard si el login es exitoso
            } else {
                model.addAttribute("error", "Credenciales incorrectas");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al iniciar sesión: " + e.getMessage());
            return "login";
        }
}


    // Maneja el cierre de sesión
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        if (session != null) {
            session.invalidate(); // Invalida la sesión actual
        }
        return "redirect:/usuarios/login"; // Redirige al formulario de login
    }

    // Muestra el formulario de registro
    @GetMapping("/registro")
    public String mostrarFormularioRegistro() {
        return "registro"; // Nombre de la plantilla de registro (registro.html)
    }

    // Maneja el registro de nuevos usuarios
    @PostMapping("/registro")
    public String registrarUsuario(@RequestParam String nombre,
                                   @RequestParam String email,
                                   @RequestParam String contrasena,
                                   Model model) {
        try {
            // Verifica si el correo ya está registrado
            if (usuarioDAO.existsByEmail(email)) {
                model.addAttribute("error", "El correo ya está registrado.");
                return "registro";
            }

            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setContrasena(contrasena);

            usuarioDAO.save(nuevoUsuario); // Usa JpaRepository para guardar

            model.addAttribute("success", "Usuario registrado exitosamente");
            return "redirect:/usuarios/login"; // Redirige al formulario de login
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar usuario: " + e.getMessage());
            return "registro";
        }
    }
}
