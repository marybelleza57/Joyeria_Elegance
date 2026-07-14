package com.joyas.elegance.controller;

import com.joyas.elegance.model.Clientes;
import com.joyas.elegance.repository.ClientesRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class ClientePerfilController {

    private final ClientesRepositorio clientesRepositorio;

    @Autowired
    public ClientePerfilController(ClientesRepositorio clientesRepositorio) {
        this.clientesRepositorio = clientesRepositorio;
    }

    @GetMapping("/cliente/configuracion")
    public String configuracionCliente(@RequestParam(required = false) String tab,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {
        Clientes clienteLogueado = (Clientes) session.getAttribute("usuarioLogueado");
        if (clienteLogueado == null) {
            redirectAttributes.addFlashAttribute("errorLogin", "Debe iniciar sesión para acceder a la configuración.");
            return "redirect:/";
        }

        if ("contraseña".equals(tab) || "contrasena".equals(tab)) {
            return "redirect:/cliente/contraseña";
        } else if ("pedidos".equals(tab)) {
            return "redirect:/cliente/pedidos";
        } else if ("devoluciones".equals(tab)) {
            return "redirect:/cliente/devoluciones";
        }

        java.util.Optional<Clientes> optCliente = clientesRepositorio.findById(clienteLogueado.getId());
        if (optCliente.isEmpty()) {
            session.removeAttribute("usuarioLogueado");
            return "redirect:/";
        }

        Clientes cliente = optCliente.get();
        model.addAttribute("cliente", cliente);
        model.addAttribute("active", "datos");
        return "cliente/configuracion";
    }

    @GetMapping({ "/cliente/contraseña", "/cliente/contrasena" })
    public String contrasenaCliente(HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {
        Clientes clienteLogueado = (Clientes) session.getAttribute("usuarioLogueado");
        if (clienteLogueado == null) {
            redirectAttributes.addFlashAttribute("errorLogin", "Debe iniciar sesión para acceder.");
            return "redirect:/";
        }

        java.util.Optional<Clientes> optCliente = clientesRepositorio.findById(clienteLogueado.getId());
        if (optCliente.isEmpty()) {
            session.removeAttribute("usuarioLogueado");
            return "redirect:/";
        }

        model.addAttribute("cliente", optCliente.get());
        model.addAttribute("active", "contraseña");
        return "cliente/contraseña";
    }

    @PostMapping("/cliente/configuracion/guardar-datos")
    public String guardarDatosPersonales(@RequestParam String nombres,
            @RequestParam String apellidos,
            @RequestParam String telefono,
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String preferencia,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String direccion,
            @RequestParam(required = false) String departamento,
            @RequestParam(required = false) String provincia,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        Clientes clienteLogueado = (Clientes) session.getAttribute("usuarioLogueado");
        if (clienteLogueado == null) {
            return "redirect:/";
        }

        try {
            java.util.Optional<Clientes> optCliente = clientesRepositorio.findById(clienteLogueado.getId());
            if (optCliente.isEmpty()) {
                session.removeAttribute("usuarioLogueado");
                return "redirect:/";
            }

            Clientes cliente = optCliente.get();
            cliente.setNombres(nombres);
            cliente.setApellidos(apellidos);
            cliente.setTelefono(telefono);

            if (dni != null && !dni.trim().isEmpty()) {
                String cleanDni = dni.trim();
                if (!cleanDni.matches("\\d{8}")) {
                    redirectAttributes.addFlashAttribute("mensajeError",
                            "El DNI debe tener exactamente 8 dígitos numéricos.");
                    return "redirect:/cliente/configuracion";
                }
                java.util.Optional<Clientes> optExistente = clientesRepositorio.findByDni(cleanDni);
                if (optExistente.isPresent() && !optExistente.get().getId().equals(cliente.getId())) {
                    redirectAttributes.addFlashAttribute("mensajeError",
                            "El DNI ingresado ya está registrado por otro cliente.");
                    return "redirect:/cliente/configuracion";
                }
                cliente.setDni(cleanDni);
            } else {
                cliente.setDni(null);
            }

            cliente.setPreferencia(preferencia);
            if (genero != null && !genero.trim().isEmpty()) {
                cliente.setGenero(genero);
            }
            if (direccion != null) {
                cliente.setDireccion(direccion);
            }
            cliente.setDepartamento(departamento);
            cliente.setProvincia(provincia);

            clientesRepositorio.save(cliente);
            session.setAttribute("usuarioLogueado", cliente);

            redirectAttributes.addFlashAttribute("mensajeExito", "Datos personales actualizados con éxito.");
            redirectAttributes.addFlashAttribute("tabActiva", "datos");
            return "redirect:/cliente/configuracion";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("mensajeError", "Error al actualizar los datos: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tabActiva", "datos");
            return "redirect:/cliente/configuracion";
        }
    }

    @PostMapping("/cliente/configuracion/cambiar-contrasena")
    public String cambiarContrasena(@RequestParam String contrasenaActual,
            @RequestParam String nuevaContrasena,
            @RequestParam String confirmarContrasena,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        Clientes clienteLogueado = (Clientes) session.getAttribute("usuarioLogueado");
        if (clienteLogueado == null) {
            return "redirect:/";
        }

        try {
            java.util.Optional<Clientes> optCliente = clientesRepositorio.findById(clienteLogueado.getId());
            if (optCliente.isEmpty()) {
                session.removeAttribute("usuarioLogueado");
                return "redirect:/";
            }

            Clientes cliente = optCliente.get();

            boolean contrasenaValida = false;
            try {
                contrasenaValida = BCrypt.checkpw(contrasenaActual, cliente.getContrasenaHash());
            } catch (Exception e) {
                contrasenaValida = false;
            }

            if (!contrasenaValida) {
                redirectAttributes.addFlashAttribute("mensajeError", "La contraseña actual es incorrecta.");
                redirectAttributes.addFlashAttribute("tabActiva", "contraseña");
                return "redirect:/cliente/contraseña";
            }

            if (nuevaContrasena == null || nuevaContrasena.trim().isEmpty() || nuevaContrasena.length() < 6) {
                redirectAttributes.addFlashAttribute("mensajeError",
                        "La nueva contraseña debe tener al menos 6 caracteres.");
                redirectAttributes.addFlashAttribute("tabActiva", "contraseña");
                return "redirect:/cliente/contraseña";
            }

            if (!confirmarContrasena.equals(nuevaContrasena)) {
                redirectAttributes.addFlashAttribute("mensajeError",
                        "La confirmación de la nueva contraseña no coincide.");
                redirectAttributes.addFlashAttribute("tabActiva", "contraseña");
                return "redirect:/cliente/contraseña";
            }

            String hash = BCrypt.hashpw(nuevaContrasena.trim(), BCrypt.gensalt());
            cliente.setContrasenaHash(hash);
            clientesRepositorio.save(cliente);

            redirectAttributes.addFlashAttribute("mensajeExito", "Contraseña actualizada con éxito.");
            redirectAttributes.addFlashAttribute("tabActiva", "contraseña");
            return "redirect:/cliente/contraseña";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("mensajeError", "Error al cambiar la contraseña: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tabActiva", "contraseña");
            return "redirect:/cliente/contraseña";
        }
    }
}