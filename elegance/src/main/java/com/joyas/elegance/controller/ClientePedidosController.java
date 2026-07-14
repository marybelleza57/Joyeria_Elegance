package com.joyas.elegance.controller;

import com.joyas.elegance.model.Clientes;
import com.joyas.elegance.model.Pedido;
import com.joyas.elegance.model.DetallePedido;
import com.joyas.elegance.model.EstadoPedido;
import com.joyas.elegance.model.NotaCredito;
import com.joyas.elegance.repository.ClientesRepositorio;
import com.joyas.elegance.repository.PedidoRepositorio;
import com.joyas.elegance.repository.DetallePedidoRepositorio;
import com.joyas.elegance.repository.NotaCreditoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class ClientePedidosController {

    private final ClientesRepositorio clientesRepositorio;
    private final PedidoRepositorio pedidoRepositorio;
    private final DetallePedidoRepositorio detallePedidoRepositorio;
    private final NotaCreditoRepositorio notaCreditoRepositorio;

    @Autowired
    public ClientePedidosController(ClientesRepositorio clientesRepositorio,
            PedidoRepositorio pedidoRepositorio,
            DetallePedidoRepositorio detallePedidoRepositorio,
            NotaCreditoRepositorio notaCreditoRepositorio) {
        this.clientesRepositorio = clientesRepositorio;
        this.pedidoRepositorio = pedidoRepositorio;
        this.detallePedidoRepositorio = detallePedidoRepositorio;
        this.notaCreditoRepositorio = notaCreditoRepositorio;
    }

    @GetMapping("/cliente/pedidos")
    public String pedidosCliente(HttpSession session,
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

        Clientes cliente = optCliente.get();
        model.addAttribute("cliente", cliente);
        model.addAttribute("pedidos", pedidoRepositorio.findByClienteOrderByFechaCreacionDesc(cliente));
        model.addAttribute("active", "pedidos");
        return "cliente/pedidos";
    }

    @GetMapping("/cliente/devoluciones")
    public String devolucionesCliente(HttpSession session,
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

        Clientes cliente = optCliente.get();

        List<Pedido> todosPedidos = pedidoRepositorio.findByClienteOrderByFechaCreacionDesc(cliente);
        List<Pedido> pedidosDevolucion = new ArrayList<>();
        Map<UUID, List<DetallePedido>> detallesPorPedido = new HashMap<>();

        for (Pedido p : todosPedidos) {
            List<DetallePedido> detalles = detallePedidoRepositorio.findByPedido(p);
            boolean tieneItemDevolucion = detalles.stream()
                    .anyMatch(d -> !"NINGUNO".equals(d.getEstadoDevolucion()));
            boolean esPedidoDevolucion = p.getEstado() == EstadoPedido.DEV_SOLICITADA ||
                    p.getEstado() == EstadoPedido.DEV_APROBADA ||
                    p.getEstado() == EstadoPedido.DEV_RECHAZADA;
            if (tieneItemDevolucion || esPedidoDevolucion) {
                pedidosDevolucion.add(p);
                detallesPorPedido.put(p.getId(), detalles);
            }
        }

        List<NotaCredito> notasCredito = notaCreditoRepositorio.findByClienteOrderByFechaEmisionDesc(cliente);
        BigDecimal saldoTotal = notasCredito.stream()
                .filter(nc -> "ACTIVO".equals(nc.getEstado()) || "USADO_PARCIAL".equals(nc.getEstado()))
                .map(NotaCredito::getMontoDisponible)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("cliente", cliente);
        model.addAttribute("devoluciones", pedidosDevolucion);
        model.addAttribute("detallesPorPedido", detallesPorPedido);
        model.addAttribute("notasCredito", notasCredito);
        model.addAttribute("saldoTotal", saldoTotal);
        model.addAttribute("active", "devoluciones");
        return "cliente/devoluciones";
    }

    @GetMapping("/api/pedidos/{id}/detalles")
    @ResponseBody
    public ResponseEntity<?> getDetallesPedido(@PathVariable UUID id, HttpSession session) {
        Clientes clienteLogueado = (Clientes) session.getAttribute("usuarioLogueado");
        if (clienteLogueado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado");
        }
        java.util.Optional<Pedido> optPedido = pedidoRepositorio.findById(id);
        if (optPedido.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido no encontrado");
        }
        Pedido pedido = optPedido.get();
        if (!pedido.getCliente().getId().equals(clienteLogueado.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado");
        }

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
            item.put("subtotal", d.getPrecioUnitario().multiply(new BigDecimal(d.getCantidad())));
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

    @GetMapping("/cliente/notas-credito")
    public String notasCreditoCliente(HttpSession session,
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

        Clientes cliente = optCliente.get();

        // Obtener todas las Notas de Crédito del cliente
        List<NotaCredito> notasCredito = notaCreditoRepositorio.findByClienteOrderByFechaEmisionDesc(cliente);

        // Calcular saldo total disponible
        BigDecimal saldoTotal = notasCredito.stream()
                .filter(nc -> "ACTIVO".equals(nc.getEstado()) || "USADO_PARCIAL".equals(nc.getEstado()))
                .map(NotaCredito::getMontoDisponible)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("cliente", cliente);
        model.addAttribute("notasCredito", notasCredito);
        model.addAttribute("saldoTotal", saldoTotal);
        model.addAttribute("active", "notas-credito");
        return "cliente/notas-credito";
    }
}