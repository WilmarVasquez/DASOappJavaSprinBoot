/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.springWeb.demo.controlador;

import com.example.springWeb.demo.dao.UsuarioDAO;
import com.example.springWeb.demo.modelo.Usuario;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
        return "redirect:/usuarios/login";
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

    @GetMapping("/registro")
public String mostrarFormularioRegistro() {
    return "registro"; // Nombre de la plantilla de registro (registro.html)
}

    
    
    // Maneja el cierre de sesión
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        if (session != null) {
            session.invalidate(); // Invalida la sesión actual
        }
        return "redirect:/usuarios/login"; // Redirige al formulario de login
    }

@GetMapping("/perfil")
public String mostrarFormularioPerfil(HttpSession session, Model model) {
    // Obtén el usuario desde la sesión
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    if (usuario != null) {
        // Asegúrate de añadir el usuario al modelo
        model.addAttribute("usuario", usuario);
        return "perfil"; // Retorna la vista del perfil
    } else {
        return "redirect:/usuarios/login"; // Redirige si no hay usuario en sesión
    }
}
    

@GetMapping("/editar/{idUsuario}")
public String mostrarFormularioEdicion(@PathVariable("idUsuario") int idUsuario, Model model) {
    try {
        Optional<Usuario> usuarioOpt = usuarioDAO.findByIdUsuario(idUsuario);
        if (usuarioOpt.isPresent()) {
            model.addAttribute("usuario", usuarioOpt.get());
            return "perfil"; // Redirige a la vista dinámica
        } else {
            return "redirect:/dashboard"; // Redirige si no existe
        }
    } catch (Exception e) {
        return "redirect:/error"; // Maneja errores genéricos
    }
}

    // Maneja la actualización del usuario desde el formulario
@PostMapping("/actualizar")
public String actualizarUsuario(@ModelAttribute Usuario usuario, Model model) {
    try {
        // Busca el usuario en la base de datos
        Optional<Usuario> usuarioOpt = usuarioDAO.findByIdUsuario(usuario.getIdUsuario());
        if (usuarioOpt.isPresent()) {
            // Actualiza los campos del usuario
            Usuario usuarioActualizado = usuarioOpt.get();
            usuarioActualizado.setNombre(usuario.getNombre());
            usuarioActualizado.setEmail(usuario.getEmail());
            usuarioActualizado.setContrasena(usuario.getContrasena());

            // Guarda los cambios
            usuarioDAO.save(usuarioActualizado);
            return "redirect:/dashboard"; // Redirige al dashboard
        } else {
            model.addAttribute("error", "Usuario no encontrado.");
            return "perfil";
        }
    } catch (Exception e) {
        model.addAttribute("error", "Error al actualizar usuario: " + e.getMessage());
        return "perfil";
    }
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
