package com.joyas.elegance.controller;

import com.joyas.elegance.model.Clientes;
import com.joyas.elegance.model.Producto;
import com.joyas.elegance.repository.ClientesRepositorio;
import com.joyas.elegance.repository.ProductoRepositorio;
import com.joyas.elegance.repository.CategoriaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;
import java.util.List;

@Controller
public class JoyasControlador {

    private final ClientesRepositorio clientesRepositorio;
    private final ProductoRepositorio productoRepositorio;
    private final CategoriaRepositorio categoriaRepositorio;

    @Autowired
    public JoyasControlador(ClientesRepositorio clientesRepositorio,
            ProductoRepositorio productoRepositorio,
            CategoriaRepositorio categoriaRepositorio) {
        this.clientesRepositorio = clientesRepositorio;
        this.productoRepositorio = productoRepositorio;
        this.categoriaRepositorio = categoriaRepositorio;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("clienteVip", new Clientes());

        List<Producto> productosMasVendidos = productoRepositorio.findTopProductosMasVendidos(6);
        if (productosMasVendidos.isEmpty()) {
            productosMasVendidos = productoRepositorio.findByDestacadoTrue();
        }
        model.addAttribute("joyasDestacadas", productosMasVendidos);

        return "index";
    }

    @GetMapping("/catalogo")
    public String catalogo(Model model) {
        model.addAttribute("productos", productoRepositorio.findAll());
        model.addAttribute("categorias", categoriaRepositorio.findAll());
        return "catalogo";
    }

    @GetMapping("/reclamaciones")
    public String reclamaciones() {
        return "reclamaciones";
    }

    @PostMapping("/vip/registrar")
    public String registrarVip(@Valid @ModelAttribute("clienteVip") Clientes clienteVip,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            return "index";
        }
        clientesRepositorio.save(clienteVip);
        model.addAttribute("mensajeExito",
                "¡Bienvenido " + clienteVip.getNombres() + "! Ya eres miembro VIP.");
        model.addAttribute("clienteVip", new Clientes());
        return "index";
    }
}