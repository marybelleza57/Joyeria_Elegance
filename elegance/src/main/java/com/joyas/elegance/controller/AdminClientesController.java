package com.joyas.elegance.controller;

import com.joyas.elegance.model.Clientes;
import com.joyas.elegance.repository.ClientesRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin/clientes")
public class AdminClientesController {

    private final ClientesRepositorio clientesRepositorio;

    @Autowired
    public AdminClientesController(ClientesRepositorio clientesRepositorio) {
        this.clientesRepositorio = clientesRepositorio;
    }

    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clientesRepositorio.findAll());
        return "admin/clientes";
    }

    @PostMapping("/actualizar")
    public String actualizarCliente(@ModelAttribute Clientes cliente, RedirectAttributes redirectAttributes) {
        Optional<Clientes> optCliente = clientesRepositorio.findById(cliente.getId());
        if (optCliente.isPresent()) {
            Clientes existente = optCliente.get();
            existente.setNombres(cliente.getNombres());
            existente.setApellidos(cliente.getApellidos());
            existente.setCorreo(cliente.getCorreo());
            existente.setTelefono(cliente.getTelefono());
            existente.setDni(cliente.getDni());
            existente.setDireccion(cliente.getDireccion());
            existente.setDepartamento(cliente.getDepartamento());
            existente.setProvincia(cliente.getProvincia());
            existente.setPreferencia(cliente.getPreferencia());
            existente.setGenero(cliente.getGenero());
            existente.setEstado(cliente.getEstado());
            clientesRepositorio.save(existente);
            redirectAttributes.addFlashAttribute("success", "Cliente actualizado exitosamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Cliente no encontrado.");
        }
        return "redirect:/admin/clientes";
    }

    @PostMapping("/suspender")
    public String suspenderCliente(@RequestParam UUID id, RedirectAttributes redirectAttributes) {
        Optional<Clientes> optCliente = clientesRepositorio.findById(id);
        if (optCliente.isPresent()) {
            Clientes cliente = optCliente.get();
            if ("SUSPENDIDO".equals(cliente.getEstado())) {
                cliente.setEstado("ACTIVO");
                redirectAttributes.addFlashAttribute("success", "Cliente activado exitosamente.");
            } else {
                cliente.setEstado("SUSPENDIDO");
                redirectAttributes.addFlashAttribute("success", "Cliente suspendido exitosamente.");
            }
            clientesRepositorio.save(cliente);
        } else {
            redirectAttributes.addFlashAttribute("error", "Cliente no encontrado.");
        }
        return "redirect:/admin/clientes";
    }
}