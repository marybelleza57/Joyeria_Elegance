package com.joyas.elegance.controller;

import com.joyas.elegance.repository.ClientesRepositorio;
import com.joyas.elegance.repository.ProductoRepositorio;
import com.joyas.elegance.repository.CategoriaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final ProductoRepositorio productoRepositorio;
    private final CategoriaRepositorio categoriaRepositorio;
    private final ClientesRepositorio clientesRepositorio;

    @Autowired
    public AdminDashboardController(ProductoRepositorio productoRepositorio,
            CategoriaRepositorio categoriaRepositorio,
            ClientesRepositorio clientesRepositorio) {
        this.productoRepositorio = productoRepositorio;
        this.categoriaRepositorio = categoriaRepositorio;
        this.clientesRepositorio = clientesRepositorio;
    }

    @GetMapping
    public String adminPanel(Model model) {
        model.addAttribute("productos", productoRepositorio.findAll());
        model.addAttribute("categorias", categoriaRepositorio.findAll());
        model.addAttribute("clientes", clientesRepositorio.findAll());
        return "admin/dashboard";
    }
}