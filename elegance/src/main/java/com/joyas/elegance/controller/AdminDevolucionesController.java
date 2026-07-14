package com.joyas.elegance.controller;

import com.joyas.elegance.model.*;
import com.joyas.elegance.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/admin/devoluciones")
public class AdminDevolucionesController {

    private final PedidoRepositorio pedidoRepositorio;
    private final DetallePedidoRepositorio detallePedidoRepositorio;
    private final NotaCreditoRepositorio notaCreditoRepositorio;

    @Autowired
    public AdminDevolucionesController(PedidoRepositorio pedidoRepositorio,
            DetallePedidoRepositorio detallePedidoRepositorio,
            NotaCreditoRepositorio notaCreditoRepositorio) {
        this.pedidoRepositorio = pedidoRepositorio;
        this.detallePedidoRepositorio = detallePedidoRepositorio;
        this.notaCreditoRepositorio = notaCreditoRepositorio;
    }

    @GetMapping
    public String listarDevoluciones(Model model) {
        List<Pedido> todos = pedidoRepositorio.findAll();
        Map<UUID, List<DetallePedido>> detallesPorPedido = new HashMap<>();
        List<Pedido> devoluciones = new ArrayList<>();

        for (Pedido p : todos) {
            List<DetallePedido> detalles = detallePedidoRepositorio.findByPedido(p);
            boolean tieneItemDevolucion = detalles.stream()
                    .anyMatch(d -> !"NINGUNO".equals(d.getEstadoDevolucion()));
            boolean esPedidoDevolucion = p.getEstado() == EstadoPedido.DEV_SOLICITADA ||
                    p.getEstado() == EstadoPedido.DEV_APROBADA ||
                    p.getEstado() == EstadoPedido.DEV_RECHAZADA;
            if (tieneItemDevolucion || esPedidoDevolucion) {
                devoluciones.add(p);
                detallesPorPedido.put(p.getId(), detalles);
            }
        }

        model.addAttribute("devoluciones", devoluciones);
        model.addAttribute("detallesPorPedido", detallesPorPedido);
        return "admin/devoluciones";
    }

    @PostMapping("/actualizar-item-estado")
    public String actualizarEstadoItemDevolucion(@RequestParam UUID itemId,
            @RequestParam String estadoDevolucion,
            RedirectAttributes redirectAttributes) {
        java.util.Optional<DetallePedido> optItem = detallePedidoRepositorio.findById(itemId);
        if (optItem.isPresent()) {
            DetallePedido item = optItem.get();
            item.setEstadoDevolucion(estadoDevolucion);
            detallePedidoRepositorio.save(item);

            Pedido pedido = item.getPedido();

            String codigoNC = "NC-" + pedido.getNumeroPedido().replace("-", "") + "-"
                    + item.getId().toString().substring(0, 4).toUpperCase();
            if ("DEV_APROBADA".equals(estadoDevolucion)) {
                if (notaCreditoRepositorio.findByCodigo(codigoNC).isEmpty()) {
                    NotaCredito nc = new NotaCredito();
                    nc.setCodigo(codigoNC);
                    nc.setCliente(pedido.getCliente());
                    nc.setPedidoOrigen(pedido);
                    BigDecimal monto = item.getPrecioUnitario().multiply(new BigDecimal(item.getCantidad()));
                    nc.setMontoInicial(monto);
                    nc.setMontoDisponible(monto);
                    nc.setEstado("ACTIVO");
                    notaCreditoRepositorio.save(nc);
                    redirectAttributes.addFlashAttribute("success",
                            "El estado del producto '" + item.getProducto().getNombre()
                                    + "' fue actualizado a APROBADA y se generó la Nota de Crédito: " + codigoNC);
                } else {
                    redirectAttributes.addFlashAttribute("success", "El estado del producto '"
                            + item.getProducto().getNombre() + "' fue actualizado a APROBADA.");
                }
            } else {
                java.util.Optional<NotaCredito> optNC = notaCreditoRepositorio.findByCodigo(codigoNC);
                if (optNC.isPresent()) {
                    NotaCredito nc = optNC.get();
                    if ("ACTIVO".equals(nc.getEstado())) {
                        notaCreditoRepositorio.delete(nc);
                    }
                }
                redirectAttributes.addFlashAttribute("success", "El estado del producto '"
                        + item.getProducto().getNombre() + "' fue actualizado a " + estadoDevolucion + ".");
            }

            List<DetallePedido> todosLosItems = detallePedidoRepositorio.findByPedido(pedido);
            boolean todosAprobados = todosLosItems.stream()
                    .allMatch(d -> "DEV_APROBADA".equals(d.getEstadoDevolucion()));
            boolean todosRechazados = todosLosItems.stream()
                    .allMatch(d -> "DEV_RECHAZADA".equals(d.getEstadoDevolucion()));
            boolean algunoSolicitado = todosLosItems.stream()
                    .anyMatch(d -> "DEV_SOLICITADA".equals(d.getEstadoDevolucion()));

            if (todosAprobados) {
                pedido.setEstado(EstadoPedido.DEV_APROBADA);
            } else if (todosRechazados) {
                pedido.setEstado(EstadoPedido.DEV_RECHAZADA);
            } else if (algunoSolicitado) {
                pedido.setEstado(EstadoPedido.DEV_SOLICITADA);
            } else {
                pedido.setEstado(EstadoPedido.DEV_APROBADA);
            }
            pedido.setFechaActualizacion(LocalDateTime.now());
            pedidoRepositorio.save(pedido);
        } else {
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado.");
        }
        return "redirect:/admin/devoluciones";
    }

    @PostMapping("/buscar-pedido")
    public String buscarPedidoDevolucion(@RequestParam String numeroPedido,
            RedirectAttributes redirectAttributes) {
        java.util.Optional<Pedido> optPedido = pedidoRepositorio.findAll().stream()
                .filter(p -> p.getNumeroPedido().equalsIgnoreCase(numeroPedido.trim()))
                .findFirst();
        if (optPedido.isPresent()) {
            Pedido pedido = optPedido.get();
            if (pedido.getEstado() != EstadoPedido.ENTREGADO &&
                    pedido.getEstado() != EstadoPedido.DEV_SOLICITADA &&
                    pedido.getEstado() != EstadoPedido.DEV_APROBADA &&
                    pedido.getEstado() != EstadoPedido.DEV_RECHAZADA) {
                redirectAttributes.addFlashAttribute("error",
                        "Solo se pueden procesar devoluciones de pedidos entregados (ENTREGADO). El estado actual es: "
                                + pedido.getEstado());
            } else {
                List<DetallePedido> detalles = detallePedidoRepositorio.findByPedido(pedido);
                boolean yaIniciado = detalles.stream().anyMatch(d -> !"NINGUNO".equals(d.getEstadoDevolucion()));

                if (!yaIniciado) {
                    for (DetallePedido d : detalles) {
                        d.setEstadoDevolucion("DEV_SOLICITADA");
                        detallePedidoRepositorio.save(d);
                    }
                    pedido.setEstado(EstadoPedido.DEV_SOLICITADA);
                    pedido.setFechaActualizacion(LocalDateTime.now());
                    pedidoRepositorio.save(pedido);
                    redirectAttributes.addFlashAttribute("success",
                            "Se ha iniciado la solicitud de devolución para todos los productos del pedido "
                                    + pedido.getNumeroPedido() + ".");
                } else {
                    redirectAttributes.addFlashAttribute("success",
                            "El pedido " + pedido.getNumeroPedido() + " ya se encuentra listado en devoluciones.");
                }
            }
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "No se encontró ningún pedido con el número " + numeroPedido + ".");
        }
        return "redirect:/admin/devoluciones";
    }
}