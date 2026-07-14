package com.joyas.elegance.controller;

import com.joyas.elegance.model.Clientes;
import com.joyas.elegance.repository.ClientesRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class ClienteAuthController {

    private final ClientesRepositorio clientesRepositorio;

    @Autowired
    public ClienteAuthController(ClientesRepositorio clientesRepositorio) {
        this.clientesRepositorio = clientesRepositorio;
    }

    @PostMapping("/cliente/registrar")
    public String registrarCliente(@RequestParam String nombres,
            @RequestParam String apellidos,
            @RequestParam String correo,
            @RequestParam String telefono,
            @RequestParam String contrasena,
            @RequestParam String genero,
            @RequestParam(defaultValue = "false") Boolean aceptaTerminos,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            if (genero == null || genero.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorRegistro", "Debes seleccionar tu sexo para registrarte.");
                return "redirect:/";
            }

            if (!genero.equals("MASCULINO") && !genero.equals("FEMENINO") && !genero.equals("PREFIERO_NO_DECIR")) {
                redirectAttributes.addFlashAttribute("errorRegistro", "El sexo seleccionado no es válido.");
                return "redirect:/";
            }

            if (clientesRepositorio.findByCorreo(correo).isPresent()) {
                redirectAttributes.addFlashAttribute("errorRegistro", "El correo electrónico ya está registrado.");
                return "redirect:/";
            }

            Clientes cliente = new Clientes();
            cliente.setNombres(nombres);
            cliente.setApellidos(apellidos);
            cliente.setCorreo(correo);
            cliente.setTelefono(telefono);
            cliente.setGenero(genero);
            cliente.setAceptaTerminos(aceptaTerminos);
            cliente.setEstado("ACTIVO");

            String hash = BCrypt.hashpw(contrasena, BCrypt.gensalt());
            cliente.setContrasenaHash(hash);

            clientesRepositorio.save(cliente);

            session.setAttribute("usuarioLogueado", cliente);
            redirectAttributes.addFlashAttribute("successMessage", "¡Registro exitoso! Bienvenido a Joyería Elegante.");
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorRegistro", "Error al registrar: " + e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/cliente/login")
    public String loginCliente(@RequestParam String correo,
            @RequestParam String contrasena,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            java.util.Optional<Clientes> optCliente = clientesRepositorio.findByCorreo(correo);
            if (optCliente.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorLogin", "El correo electrónico no está registrado.");
                return "redirect:/";
            }

            Clientes cliente = optCliente.get();
            boolean contrasenaValida = false;
            try {
                contrasenaValida = BCrypt.checkpw(contrasena, cliente.getContrasenaHash());
            } catch (Exception e) {
                contrasenaValida = false;
            }

            if (!contrasenaValida) {
                redirectAttributes.addFlashAttribute("errorLogin", "La contraseña es incorrecta.");
                return "redirect:/";
            }

            if (!"ACTIVO".equalsIgnoreCase(cliente.getEstado())) {
                redirectAttributes.addFlashAttribute("errorLogin", "Su cuenta no está activa.");
                return "redirect:/";
            }

            session.setAttribute("usuarioLogueado", cliente);
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorLogin", "Error al iniciar sesión: " + e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/cliente/logout")
    public String logoutCliente(HttpSession session) {
        session.removeAttribute("usuarioLogueado");
        return "redirect:/";
    }
}