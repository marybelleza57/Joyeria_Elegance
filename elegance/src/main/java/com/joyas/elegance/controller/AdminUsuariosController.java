package com.joyas.elegance.controller;

import com.joyas.elegance.model.Usuario;
import com.joyas.elegance.repository.UsuarioRepositorio;
import com.joyas.elegance.repository.RolRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuariosController {

    private final UsuarioRepositorio usuarioRepositorio;
    private final RolRepositorio rolRepositorio;

    @Autowired
    public AdminUsuariosController(UsuarioRepositorio usuarioRepositorio,
            RolRepositorio rolRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.rolRepositorio = rolRepositorio;
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepositorio.findAll());
        model.addAttribute("roles", rolRepositorio.findAll());
        return "admin/usuarios";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario,
            @RequestParam(required = false) String contrasenaPlana,
            @RequestParam(name = "rol.id", required = false) UUID rolId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario adminLogueado = (Usuario) session.getAttribute("adminLogueado");
        boolean esNuevo = (usuario.getId() == null);

        if (rolId != null) {
            rolRepositorio.findById(rolId).ifPresent(usuario::setRol);
        }

        if (usuario.getId() == null) {
            if (contrasenaPlana == null || contrasenaPlana.isEmpty()) {
                usuario.setContrasenaHash(BCrypt.hashpw("123456789", BCrypt.gensalt()));
            } else {
                usuario.setContrasenaHash(BCrypt.hashpw(contrasenaPlana, BCrypt.gensalt()));
            }
        } else {
            Usuario usuarioExistente = usuarioRepositorio.findById(usuario.getId()).orElse(null);
            if (usuarioExistente != null) {
                if (adminLogueado != null && adminLogueado.getId().equals(usuario.getId())) {
                    if (usuario.getEstado() != null && !"ACTIVO".equalsIgnoreCase(usuario.getEstado())) {
                        redirectAttributes.addFlashAttribute("error",
                                "No puedes desactivar tu propia cuenta de administrador.");
                        return "redirect:/admin/usuarios";
                    }
                    if (usuario.getRol() != null && usuario.getRol().getId() != null
                            && !usuarioExistente.getRol().getId().equals(usuario.getRol().getId())) {
                        redirectAttributes.addFlashAttribute("error",
                                "No puedes cambiar tu propio rol de administrador.");
                        return "redirect:/admin/usuarios";
                    }
                    usuario.setRol(usuarioExistente.getRol());
                    usuario.setEstado(usuarioExistente.getEstado());
                }

                if (contrasenaPlana != null && !contrasenaPlana.isEmpty()) {
                    usuario.setContrasenaHash(BCrypt.hashpw(contrasenaPlana, BCrypt.gensalt()));
                } else {
                    usuario.setContrasenaHash(usuarioExistente.getContrasenaHash());
                }
            }
        }
        if (usuario.getEstado() == null || usuario.getEstado().isEmpty()) {
            usuario.setEstado("ACTIVO");
        }
        usuarioRepositorio.save(usuario);
        redirectAttributes.addFlashAttribute("success",
                esNuevo ? "Usuario creado exitosamente." : "Usuario actualizado exitosamente.");
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable UUID id, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario adminLogueado = (Usuario) session.getAttribute("adminLogueado");
        if (adminLogueado != null && adminLogueado.getId().equals(id)) {
            redirectAttributes.addFlashAttribute("error", "No puedes eliminar tu propia cuenta de administrador.");
            return "redirect:/admin/usuarios";
        }
        usuarioRepositorio.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente.");
        return "redirect:/admin/usuarios";
    }
}