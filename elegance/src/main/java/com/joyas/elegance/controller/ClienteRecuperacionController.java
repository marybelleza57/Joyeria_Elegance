package com.joyas.elegance.controller;

import com.joyas.elegance.model.Clientes;
import com.joyas.elegance.repository.ClientesRepositorio;
import com.joyas.elegance.service.CorreoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ClienteRecuperacionController {

    private final ClientesRepositorio clientesRepositorio;
    private final CorreoServicio correoServicio;

    @Autowired
    public ClienteRecuperacionController(ClientesRepositorio clientesRepositorio, CorreoServicio correoServicio) {
        this.clientesRepositorio = clientesRepositorio;
        this.correoServicio = correoServicio;
    }

    @GetMapping("/cliente/olvide-contrasena")
    public String mostrarFormularioOlvideContrasena() {
        return "cliente/olvide-contrasena";
    }

    @PostMapping("/cliente/olvide-contrasena")
    public String enviarCorreoRecuperacion(@RequestParam("correo") String correo,
            RedirectAttributes redirectAttributes) {
        System.out.println("Correo recibido: " + correo);

        if (correo == null || correo.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Debes ingresar un correo electrónico.");
            return "redirect:/cliente/olvide-contrasena";
        }

        Optional<Clientes> optCliente = clientesRepositorio.findByCorreo(correo);

        if (optCliente.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El correo electrónico no está registrado.");
            return "redirect:/cliente/olvide-contrasena";
        }

        Clientes cliente = optCliente.get();

        String token = UUID.randomUUID().toString();
        cliente.setTokenRecuperacion(token);
        cliente.setFechaTokenRecuperacion(LocalDateTime.now());
        clientesRepositorio.save(cliente);

        correoServicio.enviarCorreoRecuperacion(cliente.getCorreo(), cliente.getNombres(), token);

        redirectAttributes.addFlashAttribute("mensaje",
                "Hemos enviado un enlace de recuperación a tu correo electrónico.");
        return "redirect:/cliente/olvide-contrasena";
    }

    @GetMapping("/cliente/restablecer-contrasena")
    public String mostrarFormularioRestablecer(@RequestParam String token, Model model,
            RedirectAttributes redirectAttributes) {
        Optional<Clientes> optCliente = clientesRepositorio.findAll().stream()
                .filter(c -> token.equals(c.getTokenRecuperacion()))
                .findFirst();

        if (optCliente.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El enlace de recuperación no es válido.");
            return "redirect:/cliente/olvide-contrasena";
        }

        Clientes cliente = optCliente.get();

        LocalDateTime expiracion = cliente.getFechaTokenRecuperacion().plusMinutes(15);
        if (LocalDateTime.now().isAfter(expiracion)) {
            redirectAttributes.addFlashAttribute("error", "El enlace de recuperación ha expirado. Solicita uno nuevo.");
            return "redirect:/cliente/olvide-contrasena";
        }

        model.addAttribute("token", token);
        return "cliente/restablecer-contrasena";
    }

    @PostMapping("/cliente/restablecer-contrasena")
    public String restablecerContrasena(@RequestParam String token,
            @RequestParam String nuevaContrasena,
            @RequestParam String confirmarContrasena,
            RedirectAttributes redirectAttributes) {

        if (!nuevaContrasena.equals(confirmarContrasena)) {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden.");
            return "redirect:/cliente/restablecer-contrasena?token=" + token;
        }

        if (nuevaContrasena.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "La contraseña debe tener al menos 6 caracteres.");
            return "redirect:/cliente/restablecer-contrasena?token=" + token;
        }

        Optional<Clientes> optCliente = clientesRepositorio.findAll().stream()
                .filter(c -> token.equals(c.getTokenRecuperacion()))
                .findFirst();

        if (optCliente.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El enlace de recuperación no es válido.");
            return "redirect:/cliente/olvide-contrasena";
        }

        Clientes cliente = optCliente.get();

        String hash = BCrypt.hashpw(nuevaContrasena, BCrypt.gensalt());
        cliente.setContrasenaHash(hash);
        cliente.setTokenRecuperacion(null);
        cliente.setFechaTokenRecuperacion(null);
        clientesRepositorio.save(cliente);

        redirectAttributes.addFlashAttribute("mensajeExito",
                "Tu contraseña ha sido restablecida con éxito. Inicia sesión con tu nueva contraseña.");
        return "redirect:/";
    }
}