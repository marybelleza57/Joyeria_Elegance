package com.joyas.elegance.controller;

import com.joyas.elegance.model.Pedido;
import com.joyas.elegance.model.EstadoPedido;
import com.joyas.elegance.model.DetallePedido;
import com.joyas.elegance.model.Producto;
import com.joyas.elegance.repository.PedidoRepositorio;
import com.joyas.elegance.repository.DetallePedidoRepositorio;
import com.joyas.elegance.repository.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/admin/pedidos")
public class AdminPedidosController {

    private final PedidoRepositorio pedidoRepositorio;
    private final DetallePedidoRepositorio detallePedidoRepositorio;
    private final ProductoRepositorio productoRepositorio;

    @Autowired
    public AdminPedidosController(PedidoRepositorio pedidoRepositorio,
            DetallePedidoRepositorio detallePedidoRepositorio,
            ProductoRepositorio productoRepositorio) {
        this.pedidoRepositorio = pedidoRepositorio;
        this.detallePedidoRepositorio = detallePedidoRepositorio;
        this.productoRepositorio = productoRepositorio;
    }

    @GetMapping
    public String listarPedidos(Model model) {
        model.addAttribute("pedidos", pedidoRepositorio.findAll());
        return "admin/pedidos";
    }

    @PostMapping("/actualizar-estado")
    public String actualizarEstadoPedido(@RequestParam UUID id,
            @RequestParam EstadoPedido estado,
            @RequestParam(required = false) String motivoCancelacion,
            RedirectAttributes redirectAttributes) {
        java.util.Optional<Pedido> optPedido = pedidoRepositorio.findById(id);
        if (optPedido.isPresent()) {
            Pedido pedido = optPedido.get();
            EstadoPedido estadoAnterior = pedido.getEstado();
            pedido.setEstado(estado);

            if (estado == EstadoPedido.ENTREGADO) {
                pedido.setFechaEntrega(LocalDate.now());
                pedido.setMotivoCancelacion(null);

                List<DetallePedido> detalles = detallePedidoRepositorio.findByPedido(pedido);
                for (DetallePedido detalle : detalles) {
                    Producto producto = detalle.getProducto();
                    int nuevaCantidad = producto.getCantidad() - detalle.getCantidad();
                    if (nuevaCantidad < 0) {
                        nuevaCantidad = 0;
                    }
                    producto.setCantidad(nuevaCantidad);
                    productoRepositorio.save(producto);
                }

            } else if (estado == EstadoPedido.CANCELADO) {
                pedido.setMotivoCancelacion(
                        motivoCancelacion != null && !motivoCancelacion.trim().isEmpty() ? motivoCancelacion.trim()
                                : "No especificado");
                pedido.setFechaEntrega(null);

                if (estadoAnterior == EstadoPedido.ENTREGADO) {
                    List<DetallePedido> detalles = detallePedidoRepositorio.findByPedido(pedido);
                    for (DetallePedido detalle : detalles) {
                        Producto producto = detalle.getProducto();
                        int nuevaCantidad = producto.getCantidad() + detalle.getCantidad();
                        producto.setCantidad(nuevaCantidad);
                        productoRepositorio.save(producto);
                    }
                }

            } else {
                pedido.setFechaEntrega(null);
                pedido.setMotivoCancelacion(null);
            }

            pedido.setFechaActualizacion(LocalDateTime.now());
            pedidoRepositorio.save(pedido);
            redirectAttributes.addFlashAttribute("success",
                    "Estado del pedido " + pedido.getNumeroPedido() + " actualizado a " + estado + ".");
        } else {
            redirectAttributes.addFlashAttribute("error", "Pedido no encontrado.");
        }
        return "redirect:/admin/pedidos";
    }

    @GetMapping("/{id}/detalles")
    @ResponseBody
    public ResponseEntity<?> getDetallesPedidoAdmin(@PathVariable UUID id, HttpSession session) {
        if (session.getAttribute("adminLogueado") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado");
        }
        java.util.Optional<Pedido> optPedido = pedidoRepositorio.findById(id);
        if (optPedido.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido no encontrado");
        }
        Pedido pedido = optPedido.get();
        List<DetallePedido> detalles = detallePedidoRepositorio.findByPedido(pedido);
        List<Map<String, Object>> items = new ArrayList<>();
        int totalCantidad = 0;
        for (DetallePedido d : detalles) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", d.getId().toString());
            item.put("productoNombre", d.getProducto().getNombre());
            item.put("material", d.getProducto().getMaterial());
            item.put("imagen", d.getProducto().getImagenPrincipal());
            item.put("cantidad", d.getCantidad());
            item.put("precioUnitario", d.getPrecioUnitario());
            item.put("subtotal", d.getPrecioUnitario().multiply(new java.math.BigDecimal(d.getCantidad())));
            item.put("estadoDevolucion", d.getEstadoDevolucion());
            items.add(item);
            totalCantidad += d.getCantidad();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("numeroPedido", pedido.getNumeroPedido());
        response.put("total", pedido.getTotal());
        response.put("estado", pedido.getEstado().name());
        response.put("direccionEnvio", pedido.getDireccionEnvio());
        response.put("telefonoContacto", pedido.getTelefonoContacto());
        response.put("dni", pedido.getCliente().getDni() != null ? pedido.getCliente().getDni() : "-");
        response.put("notas", pedido.getNotas());
        response.put("motivoCancelacion", pedido.getMotivoCancelacion());
        if (pedido.getFechaEntrega() != null) {
            DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("es", "PE"));
            response.put("fechaEntrega", pedido.getFechaEntrega().format(dateOnlyFormatter));
        } else {
            response.put("fechaEntrega", null);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a", new Locale("es", "PE"));
        response.put("fecha", pedido.getFechaCreacion().format(formatter));
        response.put("metodoPago", "Yape");
        response.put("totalCantidad", totalCantidad);
        response.put("items", items);

        return ResponseEntity.ok(response);
    }
}