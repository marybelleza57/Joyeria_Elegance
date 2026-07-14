package com.joyas.elegance.controller;

import com.joyas.elegance.model.Usuario;
import com.joyas.elegance.repository.UsuarioRepositorio;
import com.joyas.elegance.service.CorreoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    private final UsuarioRepositorio usuarioRepositorio;
    private final CorreoServicio correoServicio;

    @Autowired
    public AdminAuthController(UsuarioRepositorio usuarioRepositorio, CorreoServicio correoServicio) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.correoServicio = correoServicio;
    }

    @GetMapping("/login")
    public String loginAdminForm(HttpSession session, Model model) {
        if (session.getAttribute("adminLogueado") != null) {
            return "redirect:/admin";
        }
        return "admin/login";
    }

    @PostMapping("/login")
    public String loginAdminPost(@RequestParam String email,
            @RequestParam String contrasena,
            HttpSession session,
            Model model) {
        Optional<Usuario> optUsuario = usuarioRepositorio.findByCorreo(email);

        if (optUsuario.isEmpty()) {
            model.addAttribute("error", "El correo electrónico no está registrado.");
            model.addAttribute("emailVal", email);
            return "admin/login";
        }

        Usuario usuario = optUsuario.get();

        if (usuario.getRol() == null || (!"Administrador".equalsIgnoreCase(usuario.getRol().getNombre())
                && !"ADMIN".equalsIgnoreCase(usuario.getRol().getNombre()))) {
            model.addAttribute("error", "El correo electrónico no tiene permisos de administrador.");
            model.addAttribute("emailVal", email);
            return "admin/login";
        }

        if (!"ACTIVO".equalsIgnoreCase(usuario.getEstado())) {
            model.addAttribute("error", "Su cuenta de administrador no está activa o está suspendida.");
            model.addAttribute("emailVal", email);
            return "admin/login";
        }

        boolean contrasenaValida = false;
        try {
            contrasenaValida = BCrypt.checkpw(contrasena, usuario.getContrasenaHash());
        } catch (Exception e) {
            contrasenaValida = false;
        }

        if (!contrasenaValida) {
            model.addAttribute("error", "La contraseña es incorrecta.");
            model.addAttribute("emailVal", email);
            return "admin/login";
        }

        session.setAttribute("adminLogueado", usuario);
        return "redirect:/admin";
    }

    @GetMapping("/logout")
    public String logoutAdmin(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

    @PostMapping("/cambiar-contrasena")
    @ResponseBody
    public Map<String, Object> cambiarContrasena(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        Usuario usuarioSession = (Usuario) session.getAttribute("adminLogueado");
        if (usuarioSession == null) {
            response.put("success", false);
            response.put("message", "Sesión no válida o expirada.");
            return response;
        }

        Optional<Usuario> optUsuario = usuarioRepositorio.findById(usuarioSession.getId());
        if (optUsuario.isEmpty()) {
            response.put("success", false);
            response.put("message", "Usuario no encontrado.");
            return response;
        }

        Usuario usuario = optUsuario.get();

        boolean contrasenaValida = false;
        try {
            contrasenaValida = BCrypt.checkpw(currentPassword, usuario.getContrasenaHash());
        } catch (Exception e) {
            contrasenaValida = false;
        }

        if (!contrasenaValida) {
            response.put("success", false);
            response.put("message", "La contraseña actual es incorrecta.");
            return response;
        }

        String nuevoHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        usuario.setContrasenaHash(nuevoHash);
        usuarioRepositorio.save(usuario);

        session.setAttribute("adminLogueado", usuario);

        response.put("success", true);
        response.put("message", "La contraseña ha sido actualizada con éxito.");
        return response;
    }

    @GetMapping("/olvide-contrasena")
    public String mostrarFormularioOlvideContrasenaAdmin() {
        return "admin/olvide-contrasena";
    }

    @PostMapping("/olvide-contrasena")
    public String enviarCorreoRecuperacionAdmin(@RequestParam("email") String email,
            RedirectAttributes redirectAttributes) {
        Optional<Usuario> optUsuario = usuarioRepositorio.findByCorreo(email);

        if (optUsuario.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El correo electrónico no está registrado.");
            return "redirect:/admin/olvide-contrasena";
        }

        Usuario usuario = optUsuario.get();

        if (usuario.getRol() == null || (!"Administrador".equalsIgnoreCase(usuario.getRol().getNombre())
                && !"ADMIN".equalsIgnoreCase(usuario.getRol().getNombre()))) {
            redirectAttributes.addFlashAttribute("error", "El correo electrónico no tiene permisos de administrador.");
            return "redirect:/admin/olvide-contrasena";
        }

        if (!"ACTIVO".equalsIgnoreCase(usuario.getEstado())) {
            redirectAttributes.addFlashAttribute("error", "La cuenta de administrador no está activa.");
            return "redirect:/admin/olvide-contrasena";
        }

        String token = UUID.randomUUID().toString();
        usuario.setTokenRecuperacion(token);
        usuario.setFechaTokenRecuperacion(LocalDateTime.now());
        usuarioRepositorio.save(usuario);

        correoServicio.enviarCorreoRecuperacionAdmin(usuario.getCorreo(), usuario.getNombre(), token);

        redirectAttributes.addFlashAttribute("mensaje",
                "Hemos enviado un enlace de recuperación a tu correo electrónico.");
        return "redirect:/admin/olvide-contrasena";
    }

    @GetMapping("/restablecer-contrasena")
    public String mostrarFormularioRestablecerAdmin(@RequestParam String token, Model model,
            RedirectAttributes redirectAttributes) {
        System.out.println("=== GET /admin/restablecer-contrasena ===");
        System.out.println("Token: " + token);
        Optional<Usuario> optUsuario = usuarioRepositorio.findAll().stream()
                .filter(u -> token.equals(u.getTokenRecuperacion()))
                .findFirst();

        if (optUsuario.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El enlace de recuperación no es válido.");
            return "redirect:/admin/olvide-contrasena";
        }

        Usuario usuario = optUsuario.get();
        System.out.println("Nombre: " + usuario.getNombre());
        System.out.println("Rol: " + (usuario.getRol() != null ? usuario.getRol().getNombre() : "NULL"));
        System.out.println("Estado: " + usuario.getEstado());

        LocalDateTime expiracion = usuario.getFechaTokenRecuperacion().plusMinutes(15);
        if (LocalDateTime.now().isAfter(expiracion)) {
            redirectAttributes.addFlashAttribute("error", "El enlace de recuperación ha expirado. Solicita uno nuevo.");
            return "redirect:/admin/olvide-contrasena";
        }

        model.addAttribute("token", token);
        return "admin/restablecer-contrasena";
    }

    @PostMapping("/restablecer-contrasena")
    public String restablecerContrasenaAdmin(@RequestParam String token,
            @RequestParam String nuevaContrasena,
            @RequestParam String confirmarContrasena,
            RedirectAttributes redirectAttributes) {

        if (!nuevaContrasena.equals(confirmarContrasena)) {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden.");
            return "redirect:/admin/restablecer-contrasena?token=" + token;
        }

        if (nuevaContrasena.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "La contraseña debe tener al menos 6 caracteres.");
            return "redirect:/admin/restablecer-contrasena?token=" + token;
        }

        Optional<Usuario> optUsuario = usuarioRepositorio.findAll().stream()
                .filter(u -> token.equals(u.getTokenRecuperacion()))
                .findFirst();

        if (optUsuario.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El enlace de recuperación no es válido.");
            return "redirect:/admin/olvide-contrasena";
        }

        Usuario usuario = optUsuario.get();

        String hash = BCrypt.hashpw(nuevaContrasena, BCrypt.gensalt());
        usuario.setContrasenaHash(hash);
        usuario.setTokenRecuperacion(null);
        usuario.setFechaTokenRecuperacion(null);
        usuarioRepositorio.save(usuario);

        redirectAttributes.addFlashAttribute("mensaje",
                "Tu contraseña ha sido restablecida con éxito. Inicia sesión con tu nueva contraseña.");
        return "redirect:/admin/login";
    }
}