package com.joyas.elegance.controller;

import com.joyas.elegance.model.Categoria;
import com.joyas.elegance.repository.CategoriaRepositorio;
import com.joyas.elegance.repository.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/admin/categorias")
public class AdminCategoriasController {

    private final CategoriaRepositorio categoriaRepositorio;
    private final ProductoRepositorio productoRepositorio;

    @Autowired
    public AdminCategoriasController(CategoriaRepositorio categoriaRepositorio,
            ProductoRepositorio productoRepositorio) {
        this.categoriaRepositorio = categoriaRepositorio;
        this.productoRepositorio = productoRepositorio;
    }

    @GetMapping
    public String listarCategorias(Model model) {
        model.addAttribute("categorias", categoriaRepositorio.findAll());
        return "admin/categorias";
    }

    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute Categoria categoria,
            RedirectAttributes redirectAttributes) {
        boolean esNuevo = (categoria.getId() == null);
        categoriaRepositorio.save(categoria);
        redirectAttributes.addFlashAttribute("success",
                esNuevo ? "Categoría creada exitosamente." : "Categoría actualizada exitosamente.");
        return "redirect:/admin/categorias";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable UUID id,
            RedirectAttributes redirectAttributes) {
        java.util.Optional<Categoria> optCat = categoriaRepositorio.findById(id);
        if (optCat.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Categoría no encontrada.");
            return "redirect:/admin/categorias";
        }

        Categoria categoria = optCat.get();
        long productosEnPedidos = productoRepositorio.countByCategoriaIdInPedidos(id.toString());
        if (productosEnPedidos > 0) {
            categoria.setEstado("INACTIVO");
            categoriaRepositorio.save(categoria);
            redirectAttributes.addFlashAttribute("success",
                    "La categoría \"" + categoria.getNombre() + "\" tiene productos en pedidos y fue deshabilitada.");
            return "redirect:/admin/categorias";
        }

        long totalProductos = productoRepositorio.countByCategoriaId(id);
        if (totalProductos > 0) {
            redirectAttributes.addFlashAttribute("error",
                    "No se puede eliminar: la categoría \"" + categoria.getNombre()
                            + "\" tiene productos asociados. Elimine o reasigne los productos primero.");
            return "redirect:/admin/categorias";
        }

        categoriaRepositorio.deleteById(id);
        redirectAttributes.addFlashAttribute("success",
                "Categoría \"" + categoria.getNombre() + "\" eliminada exitosamente.");
        return "redirect:/admin/categorias";
    }
}