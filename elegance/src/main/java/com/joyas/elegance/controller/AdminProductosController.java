package com.joyas.elegance.controller;

import com.joyas.elegance.model.Producto;
import com.joyas.elegance.repository.ProductoRepositorio;
import com.joyas.elegance.repository.CategoriaRepositorio;
import com.joyas.elegance.service.CloudinaryServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/admin/productos")
public class AdminProductosController {

    private final ProductoRepositorio productoRepositorio;
    private final CategoriaRepositorio categoriaRepositorio;
    private final CloudinaryServicio cloudinaryServicio;

    @Autowired
    public AdminProductosController(ProductoRepositorio productoRepositorio,
            CategoriaRepositorio categoriaRepositorio,
            CloudinaryServicio cloudinaryServicio) {
        this.productoRepositorio = productoRepositorio;
        this.categoriaRepositorio = categoriaRepositorio;
        this.cloudinaryServicio = cloudinaryServicio;
    }

    @GetMapping
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoRepositorio.findAll());
        model.addAttribute("categorias", categoriaRepositorio.findAll());
        java.util.Map<UUID, String> mapaCategorias = new java.util.HashMap<>();
        for (com.joyas.elegance.model.Categoria cat : categoriaRepositorio.findAll()) {
            mapaCategorias.put(cat.getId(), cat.getNombre());
        }
        model.addAttribute("mapaCategorias", mapaCategorias);
        return "admin/productos";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto,
            @RequestParam(value = "imagenArchivo", required = false) MultipartFile imagenArchivo) {
        try {
            Producto productoExistente = null;
            if (producto.getId() != null) {
                productoExistente = productoRepositorio.findById(producto.getId()).orElse(null);
            }

            if (producto.getNombre() != null) {
                producto.setEnlaceAmigable(generateSlug(producto.getNombre()));
            }

            // Procesar imagen
            if (imagenArchivo != null && !imagenArchivo.isEmpty()) {
                try {
                    String slugNombre = generateSlug(producto.getNombre());
                    String secureUrl = cloudinaryServicio.subirImagenWebp(imagenArchivo, slugNombre);
                    producto.setImagenPrincipal(secureUrl);
                } catch (Exception e) {
                    System.err.println("Error al subir imagen a Cloudinary: " + e.getMessage());
                    if (productoExistente != null) {
                        producto.setImagenPrincipal(productoExistente.getImagenPrincipal());
                    } else {
                        producto.setImagenPrincipal("img/productos/ejemplo.jpg");
                    }
                }
            } else {
                if (productoExistente != null) {
                    producto.setImagenPrincipal(productoExistente.getImagenPrincipal());
                } else if (producto.getImagenPrincipal() == null || producto.getImagenPrincipal().isEmpty()) {
                    producto.setImagenPrincipal("img/productos/ejemplo.jpg");
                }
            }

            if (producto.getEstado() == null) {
                producto.setEstado("ACTIVO");
            }
            if (producto.getDestacado() == null) {
                producto.setDestacado(false);
            }
            productoRepositorio.save(producto);
            return "redirect:/admin/productos";
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("ERROR AL GUARDAR PRODUCTO: " + e.toString(), e);
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable UUID id) {
        productoRepositorio.deleteById(id);
        return "redirect:/admin/productos";
    }

    @GetMapping("/deshabilitar/{id}")
    public String deshabilitarProducto(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        java.util.Optional<Producto> optProd = productoRepositorio.findById(id);
        if (optProd.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado.");
            return "redirect:/admin/productos";
        }
        Producto producto = optProd.get();
        if ("ACTIVO".equals(producto.getEstado())) {
            producto.setEstado("INACTIVO");
            redirectAttributes.addFlashAttribute("success", "Producto \"" + producto.getNombre() + "\" deshabilitado.");
        } else {
            producto.setEstado("ACTIVO");
            redirectAttributes.addFlashAttribute("success", "Producto \"" + producto.getNombre() + "\" habilitado.");
        }
        productoRepositorio.save(producto);
        return "redirect:/admin/productos";
    }

    private String generateSlug(String input) {
        if (input == null)
            return "producto";
        String normalized = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD);
        String slug = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        slug = slug.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim();
        return slug.isEmpty() ? "producto" : slug;
    }
}